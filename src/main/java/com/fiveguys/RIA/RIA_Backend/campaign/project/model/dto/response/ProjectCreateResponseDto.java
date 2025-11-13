package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectCreateResponseDto {
  private final Long projectId;
  private final String title;
  private final String status;
  private final LocalDateTime createdAt;
  private final Long pipelineId;
}
