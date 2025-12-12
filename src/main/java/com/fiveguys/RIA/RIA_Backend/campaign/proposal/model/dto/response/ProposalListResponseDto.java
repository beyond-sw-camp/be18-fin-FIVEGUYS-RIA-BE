package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProposalListResponseDto {
  private Long proposalId;
  private String proposalTitle;
  private String clientCompanyName;
  private String clientName;
  private String createdUserName;
  private LocalDate requestDate;
  private LocalDate submitDate;
  private Proposal.Status status;
}
