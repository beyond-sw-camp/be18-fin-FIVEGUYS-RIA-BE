package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProposalCreateResponseDto {
  private Long proposalId;
  private Long projectId;
  private Long pipelineId;
  private String status;
  private LocalDateTime createdAt;
}
