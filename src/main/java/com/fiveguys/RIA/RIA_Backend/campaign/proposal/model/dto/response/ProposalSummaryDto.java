package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalSummaryDto {
  private Long proposalId;
  private String title;
  private String writerName;
  private LocalDate requestDate;
  private LocalDate submitDate;
  private String description;
}