package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.PermissionValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.component.PipelinePolicy;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component.ProposalLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component.ProposalValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
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
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  //생성
  @Override
  @Transactional
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

    Page<ProposalListResponseDto> result =
        proposalRepository.findProposalList(
            projectId,
            clientCompanyId,
            keyword,
            status,
            Proposal.Status.CANCELED,
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

  //수정
  @Transactional
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

    // 7. 프로젝트 변경 시 파이프라인 자동 진행
    if (newProject != null
        && (oldProject == null || !newProject.getProjectId().equals(oldProject.getProjectId()))) {

      Pipeline pipeline = newProject.getPipeline();

      if (pipeline != null && pipeline.getCurrentStage() == 1) {
        pipeline.autoAdvance(
            2,
            Pipeline.StageName.INTERNAL_REVIEW,
            Pipeline.Status.ACTIVE
        );
      }
    }

    // 8. 응답 생성 (project nullable 대응은 mapper가 처리)
    return proposalMapper.toDetailDto(p);
  }



  //삭제
  @Override
  @Transactional
  public void deleteProposal(Long proposalId, CustomUserDetails user) {

    // 1. 제안서 조회
    Proposal p = proposalLoader.loadProposal(proposalId);

    // 2. 권한 검증
    permissionValidator.validateOwnerOrLeadOrAdmin(p.getCreatedUser(), user);

    // 3. 이미 취소된 제안서는 또 취소 불가
    proposalValidator.validateStatus(p);
    
    // 4. Soft delete
    p.cancel();
  }
}
