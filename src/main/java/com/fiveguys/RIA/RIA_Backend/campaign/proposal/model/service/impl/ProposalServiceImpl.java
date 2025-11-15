package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.PermissionValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.component.PipelinePolicy;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component.ProposalDomainLoader;
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
import com.fiveguys.RIA.RIA_Backend.common.model.dto.PageResponseDto;
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
  private final ProposalDomainLoader proposalDomainLoader;
  private final ProposalValidator proposalValidator;
  private final PipelinePolicy pipelinePolicy;

  //생성
  @Override
  @Transactional
  public ProposalCreateResponseDto createProposal(ProposalCreateRequestDto dto, Long userId) {

    // 1. 입력 검증
    proposalValidator.validateCreate(dto);

    // 2. 엔티티 로딩
    User user = proposalDomainLoader.loadUser(userId);
    ClientCompany company = proposalDomainLoader.loadCompany(dto.getClientCompanyId());
    Client client = proposalDomainLoader.loadClient(dto.getClientId());
    Project project = proposalDomainLoader.loadProject(dto.getProjectId());
    Pipeline pipeline = proposalDomainLoader.loadPipeline(dto.getPipelineId());

    // 3. 고객사-고객 일치 검증
    if (!client.getClientCompany().getId().equals(company.getId())) {
      throw new CustomException(ProposalErrorCode.CLIENT_COMPANY_MISMATCH);
    }

    // 4. 제목 중복 검증
    if (proposalRepository.existsByTitleAndClientCompany(dto.getTitle(), company)) {
      throw new CustomException(ProposalErrorCode.DUPLICATE_PROPOSAL);
    }

    // 5. 상태 결정
    Proposal.Status status =
        (project == null) ? Proposal.Status.DRAFT : Proposal.Status.SUBMITTED;

    // 6. Proposal 생성
    Proposal proposal = Proposal.create(
        project,
        pipeline,
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

    // 7. 파이프라인 단계 자동 정책 적용
    pipelinePolicy.handleProposalCreated(pipeline, project);

    // 8. DTO 변환
    return proposalMapper.toCreateDto(proposal);
  }

  //목록 조회
  @Override
  @Transactional(readOnly = true)
  public PageResponseDto<ProposalListResponseDto> getProposalList(
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
            pageable
        );

    return PageResponseDto.<ProposalListResponseDto>builder()
        .page(page)
        .size(size)
        .totalCount(result.getTotalElements())
        .data(result.getContent())
        .build();
  }

  //상세 조회
  @Override
  public ProposalDetailResponseDto getProposalDetail(Long id) {

    Proposal p = proposalRepository.findDetailById(id);
    if (p == null) {
      throw new CustomException(ProposalErrorCode.PROPOSAL_NOT_FOUND);
    }

    return ProposalDetailResponseDto.builder()
        .proposalId(p.getProposalId())
        .projectId(p.getProject().getProjectId())
        .projectTitle(p.getProject().getTitle())

        .clientCompanyId(p.getClientCompany().getId())
        .clientCompanyName(p.getClientCompany().getCompanyName())

        .clientId(p.getClient().getId())
        .clientName(p.getClient().getName())

        .createdUserId(p.getCreatedUser().getId())
        .createdUserName(p.getCreatedUser().getName())

        .title(p.getTitle())
        .data(p.getData())

        .requestDate(p.getRequestDate())
        .submitDate(p.getSubmitDate())

        .remark(p.getRemark())
        .build();
  }

  //수정
  @Transactional
  public ProposalDetailResponseDto updateProposal(
      Long proposalId,
      ProposalUpdateRequestDto dto,
      CustomUserDetails user
  ) {

    // 1. 제안서 조회
    Proposal p = proposalDomainLoader.loadProposal(proposalId);

    // 2. 권한 검증
    permissionValidator.validateOwnerOrLeadOrAdmin(p.getCreatedUser(), user);

    // 3. 상태 검증
    if (p.getStatus() == Proposal.Status.CANCELED) {
      throw new CustomException(ProposalErrorCode.CANNOT_MODIFY_CANCELED_PROPOSAL);
    }

    // 4. 입력 필드 검증
    proposalValidator.validateUpdate(dto);

    // 5. 연관 로딩 - null 값은 로딩하지 않음
    Project newProject = null;
    ClientCompany newCompany = null;
    Client newClient = null;

    if (dto.getProjectId() != null) {
      newProject = proposalDomainLoader.loadProject(dto.getProjectId());
    }

    if (dto.getClientCompanyId() != null) {
      newCompany = proposalDomainLoader.loadCompany(dto.getClientCompanyId());
    }

    if (dto.getClientId() != null) {
      newClient = proposalDomainLoader.loadClient(dto.getClientId());
    }

    // 6. 고객회사 불일치 검증
    ClientCompany targetCompany =
        (newCompany != null) ? newCompany : p.getClientCompany();

    if (newClient != null &&
        !newClient.getClientCompany().getId().equals(targetCompany.getId())) {
      throw new CustomException(ProposalErrorCode.CLIENT_COMPANY_MISMATCH);
    }

    Project oldProject = p.getProject();

    // 7. 업데이트
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

    if (newProject != null && !newProject.getProjectId().equals(oldProject.getProjectId())) {

      Pipeline pipeline = newProject.getPipeline();

      if (pipeline != null && pipeline.getCurrentStage() == 1) {
        pipeline.updateStage(
            2,
            Pipeline.StageName.INTERNAL_REVIEW,
            Pipeline.Status.ACTIVE
        );
      }
    }

    // 8. 응답 생성
    return proposalMapper.toDetailDto(p);
  }

  @Override
  @Transactional
  public void deleteProposal(Long proposalId, CustomUserDetails user) {

    // 1. 제안서 조회
    Proposal proposal = proposalDomainLoader.loadProposal(proposalId);

    // 2. 권한 검증
    permissionValidator.validateOwnerOrLeadOrAdmin(proposal.getCreatedUser(), user);

    // 3. 이미 취소된 제안서는 또 취소 불가
    if (proposal.getStatus() == Proposal.Status.CANCELED) {
      throw new CustomException(ProposalErrorCode.CANNOT_MODIFY_CANCELED_PROPOSAL);
    }

    // 4. Soft delete
    proposal.cancel();
  }
}
