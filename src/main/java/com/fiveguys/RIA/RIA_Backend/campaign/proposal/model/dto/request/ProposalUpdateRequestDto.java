package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProposalUpdateRequestDto {

  private Long projectId;
  private Long clientCompanyId;
  private Long clientId;

  private String title;
  private String data;

  private LocalDate requestDate;
  private LocalDate submitDate;

  private String remark;
}
