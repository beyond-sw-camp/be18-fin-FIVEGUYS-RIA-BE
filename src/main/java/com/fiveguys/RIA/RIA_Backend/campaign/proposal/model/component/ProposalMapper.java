package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import org.springframework.stereotype.Component;

@Component
public class ProposalMapper {

  //목록
  public ProposalCreateResponseDto toCreateDto(Proposal p) {
    return ProposalCreateResponseDto.builder()
        .proposalId(p.getProposalId())
        .projectId(p.getProject() != null ? p.getProject().getProjectId() : null)
        .pipelineId(p.getPipeline() != null ? p.getPipeline().getPipelineId() : null)
        .status(p.getStatus().name())
        .createdAt(p.getCreatedAt())
        .build();
  }

  //상세
  public ProposalDetailResponseDto toDetailDto(Proposal p) {
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
}
