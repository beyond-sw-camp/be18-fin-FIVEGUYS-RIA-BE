package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProposalCreateResponseDto {
  private final Long proposalId;
  private final Long projectId;
  private final Long pipelineId;
  private final String status;
  private final LocalDateTime createdAt;
}
