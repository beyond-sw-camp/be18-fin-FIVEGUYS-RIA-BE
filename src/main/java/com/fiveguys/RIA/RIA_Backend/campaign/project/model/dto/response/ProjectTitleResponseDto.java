package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectTitleResponseDto {
  private Long projectId;
  private String projectTitle;
}
