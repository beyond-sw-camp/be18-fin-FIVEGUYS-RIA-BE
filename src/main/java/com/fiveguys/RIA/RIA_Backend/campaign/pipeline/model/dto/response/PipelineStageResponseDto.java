package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PipelineStageResponseDto {
  private Integer stageNo;
  private String stageName;
  private boolean isCompleted;
  private LocalDateTime completedAt;
}