package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.PermissionValidator;
import com.fiveguys.RIA.RIA_Backend.common.util.PipelinePolicy;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component.ProposalLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component.ProposalValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalSimpleDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component.ProposalMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository.ProposalRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.ProposalService;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProposalErrorCode;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.event.proposal.ProposalNotificationEvent;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProposalServiceImpl implements ProposalService {

  private final ProposalRepository proposalRepository;
  private final PermissionValidator permissionValidator;
  private final ProposalMapper proposalMapper;
  private final ProposalLoader proposalLoader;
  private final ProposalValidator proposalValidator;
  private final PipelinePolicy pipelinePolicy;
  //이벤트
  private final ApplicationEventPublisher eventPublisher;

  //생성
  @Override
  public ProposalCreateResponseDto createProposal(ProposalCreateRequestDto dto, Long userId) {

    // 1. 입력 검증
    proposalValidator.validateCreate(dto);

    // 2. 엔티티 로딩
    User user = proposalLoader.loadUser(userId);
    ClientCompany company = proposalLoader.loadCompany(dto.getClientCompanyId());
    Client client = proposalLoader.loadClient(dto.getClientId());

    Project project = null;
    Pipeline pipeline = null;

    if (dto.getProjectId() != null) {
      // 프로젝트 + 파이프라인 같이 로딩
      project = proposalLoader.loadProjectWithPipeline(dto.getProjectId());
      pipeline = project.getPipeline();

      if (pipeline == null) {
        throw new CustomException(ProposalErrorCode.PIPELINE_NOT_FOUND);
      }
    }

    // 3. 고객사-고객 일치 검증
    proposalValidator.validateClientMatchesCompany(client, company);
    // 4. 제목 중복 검증
    proposalValidator.validateDuplicateTitle(dto.getTitle(), company);


    // 5. 상태 결정
    Proposal.Status status =
        (project == null) ? Proposal.Status.DRAFT : Proposal.Status.SUBMITTED;

    // 6. Proposal 생성
    Proposal proposal = Proposal.create(
        project,
        user,
        client,
        company,
        dto.getTitle(),
        dto.getData(),
        dto.getRequestDate(),
        dto.getSubmitDate(),
        dto.getRemark(),
        status
    );

    proposalRepository.save(proposal);

    // 7. 파이프라인 단계 자동 정책 적용 (프로젝트가 있을 때만)
    if (pipeline != null) {
      pipelinePolicy.handleProposalCreated(pipeline, project);
    }

    // 이벤트
    String roleName = switch (user.getRole().getRoleName()) {
      case ROLE_ADMIN -> "관리자";
      case ROLE_SALES_LEAD -> "영업팀장";
      case ROLE_SALES_MEMBER -> "영업 사원";
    };

    // 생성 이벤트, 임시 본인->본인
    publishProposalNotification(user, user, proposal, NotificationTargetAction.CREATED);

    // 8. DTO 변환
    return proposalMapper.toCreateDto(proposal);
  }


  //목록 조회
  @Override
  @Transactional(readOnly = true)
  public ProposalPageResponseDto<ProposalListResponseDto> getProposalList(
      Long projectId,
      Long clientCompanyId,
      String keyword,
      Proposal.Status status,
      int page,
      int size
  ) {

    Pageable pageable = PageRequest.of(page - 1, size);


    Proposal.Status excludeCanceled =
        (status == null || status != Proposal.Status.CANCELED)
            ? Proposal.Status.CANCELED
            : null;

    Page<ProposalListResponseDto> result =
        proposalRepository.findProposalList(
            projectId,
            clientCompanyId,
            keyword,
            status,
            excludeCanceled,
            pageable
        );

    return ProposalPageResponseDto.<ProposalListResponseDto>builder()
        .page(page)
        .size(size)
        .totalCount(result.getTotalElements())
        .data(result.getContent())
        .build();
  }

  //상세 조회
  @Override
  public ProposalDetailResponseDto getProposalDetail(Long id) {

    Proposal p = proposalLoader.loadProposalDetail(id);

    return proposalMapper.toDetailDto(p);
  }

  // 수정
  @Override
  public ProposalDetailResponseDto updateProposal(
      Long proposalId,
      ProposalUpdateRequestDto dto,
      CustomUserDetails user
  ) {
    // 1. 제안서 조회
    Proposal p = proposalLoader.loadProposal(proposalId);

    // 2. 권한 검증
    permissionValidator.validateOwnerOrLeadOrAdmin(p.getCreatedUser(), user);

    // 3. 상태 + 입력 검증 (권한 통과 후)
    proposalValidator.validateStatus(p);
    proposalValidator.validateUpdate(dto);

    // 4. 연관 로딩 - null 값은 로딩하지 않음
    Project newProject = null;
    ClientCompany newCompany = null;
    Client newClient = null;

    if (dto.getProjectId() != null) {
      newProject = proposalLoader.loadProject(dto.getProjectId());
    }

    if (dto.getClientCompanyId() != null) {
      newCompany = proposalLoader.loadCompany(dto.getClientCompanyId());
    }

    if (dto.getClientId() != null) {
      newClient = proposalLoader.loadClient(dto.getClientId());
    }

    // 5. 고객사-고객 일치 검증
    proposalValidator.validateClientCompanyChange(
        p.getClient(),         // 기존 고객 (null 아님)
        newClient,             // 새 고객 (nullable)
        p.getClientCompany(),  // 기존 고객사 (null 아님)
        newCompany             // 새 고객사 (nullable)
    );

    proposalValidator.validateDuplicateOnUpdate(
        p,
        dto.getTitle()
    );

    Project oldProject = p.getProject();

    // 6. 엔티티 업데이트
    p.update(
        dto.getTitle(),
        dto.getData(),
        dto.getRequestDate(),
        dto.getSubmitDate(),
        dto.getRemark(),
        newProject,
        newCompany,
        newClient
    );

    // 7. 프로젝트 변경 시 파이프라인 / 상태 자동 진행
    if (newProject != null
        && (oldProject == null || !newProject.getProjectId().equals(oldProject.getProjectId()))) {

      // 제안서 ↔ 프로젝트 링크 시 파이프라인 최소 1단계로
      pipelinePolicy.handleProposalLinked(newProject);

      // 프로젝트가 "처음" 붙는 경우: DRAFT → SUBMITTED
      if (oldProject == null && p.getStatus() == Proposal.Status.DRAFT) {
        p.changeStatus(Proposal.Status.SUBMITTED);
      }
    }

    // 8. 응답 생성 (project nullable 대응은 mapper가 처리)
    return proposalMapper.toDetailDto(p);
  }



  //삭제
  @Override
  public void deleteProposal(Long proposalId, CustomUserDetails user) {

    // 1. 제안서 조회
    Proposal p = proposalLoader.loadProposal(proposalId);

    // 2. 권한 검증
    permissionValidator.validateOwnerOrLeadOrAdmin(p.getCreatedUser(), user);

    // 3. 이미 취소된 제안서는 또 취소 불가
    proposalValidator.validateStatus(p);
    
    // 4. Soft delete
    p.cancel();

    // 삭제 이벤트
    publishProposalNotification(
            proposalLoader.loadUser(user.getUserId()),
            p.getCreatedUser(),
            p,
            NotificationTargetAction.DELETED
    );

  }

  @Override
  @Transactional(readOnly = true)
  public List<ProposalSimpleDto> getSimpleProposals(Long projectId) {

    List<Proposal> proposals = proposalLoader.loadByProjectId(projectId);

    return proposals.stream()
            .map(proposalMapper::toDto)
            .toList();
  }

  // 제안 이벤트
  private void publishProposalNotification(User sender, User receiver, Proposal proposal, NotificationTargetAction targetAction) {
    // 1. 본인에게 보내는 알림은 스킵
    if (sender.getId().equals(receiver.getId())) {
      return;
    }

    // 2. RoleName 변환
    String roleName = switch (sender.getRole().getRoleName()) {
      case ROLE_ADMIN -> "관리자";
      case ROLE_SALES_LEAD -> "영업팀장";
      case ROLE_SALES_MEMBER -> "영업사원";
    };

    // 3. 이벤트 발행
    eventPublisher.publishEvent(
            new ProposalNotificationEvent(
                    this,
                    sender.getId(),
                    sender.getName(),
                    roleName,
                    receiver.getId(),
                    proposal,
                    targetAction
            )
    );
  }
}
