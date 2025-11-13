package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request;

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
public class ProposalCreateRequestDto {
  private Long projectId;         // nullable
  private Long pipelineId;        // nullable
  private Long clientCompanyId;   // required
  private Long clientId;          // required
  private String title;           // required
  private String data;            // optional
  private LocalDate requestDate;  // optional
  private LocalDate submitDate;   // optional
  private LocalDate presentDate;  // optional
  private LocalDate periodStart;  // optional
  private LocalDate periodEnd;    // optional
}
