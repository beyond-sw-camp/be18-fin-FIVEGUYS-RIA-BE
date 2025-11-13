package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository.ProposalRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.repository.PipelineRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProposalErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProposalServiceImpl {

  private final ProposalRepository proposalRepository;
  private final ProjectRepository projectRepository;
  private final PipelineRepository pipelineRepository;
  private final ClientCompanyRepository clientCompanyRepository;
  private final ClientRepository clientRepository;
  private final UserRepository userRepository;

  public ProposalCreateResponseDto createProposal(ProposalCreateRequestDto dto, Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ProposalErrorCode.USER_NOT_FOUND));

    ClientCompany company = clientCompanyRepository.findById(dto.getClientCompanyId())
        .orElseThrow(() -> new CustomException(ProposalErrorCode.CLIENT_COMPANY_NOT_FOUND));

    Client client = clientRepository.findById(dto.getClientId())
        .orElseThrow(() -> new CustomException(ProposalErrorCode.CLIENT_NOT_FOUND));

    Project project = null;
    if (dto.getProjectId() != null) {
      project = projectRepository.findById(dto.getProjectId())
          .orElseThrow(() -> new CustomException(ProposalErrorCode.PROJECT_NOT_FOUND));
    }

    Pipeline pipeline = null;
    if (dto.getPipelineId() != null) {
      pipeline = pipelineRepository.findById(dto.getPipelineId())
          .orElseThrow(() -> new CustomException(ProposalErrorCode.PIPELINE_NOT_FOUND));
    }

    //  중복 검사
    if (proposalRepository.existsByTitleAndClientCompany(dto.getTitle(), company)) {
      throw new CustomException(ProposalErrorCode.DUPLICATE_PROPOSAL);
    }

    // 상태 결정
    Proposal.Status status = (dto.getProjectId() == null)
        ? Proposal.Status.DRAFT
        : Proposal.Status.SUBMITTED;

    Proposal proposal = Proposal.builder()
        .project(project)
        .pipeline(pipeline)
        .createdUser(user)
        .client(client)
        .clientCompany(company)
        .title(dto.getTitle())
        .data(dto.getData())
        .status(status)
        .requestDate(dto.getRequestDate())
        .submitDate(dto.getSubmitDate())
        .presentDate(dto.getPresentDate())
        .periodStart(dto.getPeriodStart())
        .periodEnd(dto.getPeriodEnd())
        .build();

    proposalRepository.save(proposal);

    //  파이프라인 단계 자동 갱신
    if (pipeline != null && project != null) {
      // 현재 단계가 '1: 제안수신'이면 → '2: 내부검토'로 자동 이동
      if (pipeline.getCurrentStage() == 1) {
        pipeline.updateStage(2, Pipeline.StageName.INTERNAL_REVIEW, Pipeline.Status.ACTIVE);
        pipelineRepository.save(pipeline);
      }
    }

    return ProposalCreateResponseDto.builder()
        .proposalId(proposal.getProposalId())
        .projectId(project != null ? project.getProjectId() : null)
        .pipelineId(pipeline != null ? pipeline.getPipelineId() : null)
        .status(proposal.getStatus().name())
        .createdAt(proposal.getCreatedAt())
        .build();
  }
}
