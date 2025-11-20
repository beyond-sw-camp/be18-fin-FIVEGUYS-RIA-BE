package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PipelineStageUpdateResponseDto {
  private Long pipelineId;
  private Integer previousStage;
  private Integer currentStage;
  private Long updatedBy;
  private LocalDateTime updatedAt;
}