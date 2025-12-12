package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalSimpleDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProposalErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
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

    Project project = p.getProject();
    ClientCompany company = p.getClientCompany();
    Client client = p.getClient();
    User createdUser = p.getCreatedUser();

    if (company == null) {
      throw new CustomException(ProposalErrorCode.CLIENT_COMPANY_NOT_FOUND);
    }
    if (client == null) {
      throw new CustomException(ProposalErrorCode.CLIENT_NOT_FOUND);
    }
    if (createdUser == null) {
      throw new CustomException(ProposalErrorCode.CREATED_USER_NOT_FOUND);
    }

    return ProposalDetailResponseDto.builder()
        .proposalId(p.getProposalId())

        // project: 있을 수도, 없을 수도
        .projectId(project != null ? project.getProjectId() : null)
        .projectTitle(project != null ? project.getTitle() : null)

        // clientCompany: 반드시 존재
        .clientCompanyId(company.getId())
        .clientCompanyName(company.getCompanyName())

        // client: 반드시 존재
        .clientId(client.getId())
        .clientName(client.getName())

        // createdUser: 반드시 존재
        .createdUserId(createdUser.getId())
        .createdUserName(createdUser.getName())

        .title(p.getTitle())
        .data(p.getData())
        .requestDate(p.getRequestDate())
        .submitDate(p.getSubmitDate())
        .remark(p.getRemark())
        .build();
  }

  public ProposalSimpleDto toDto(Proposal p) {
    return ProposalSimpleDto.builder()
            .id(p.getProposalId())
            .title(p.getTitle())
            .build();
  }
}