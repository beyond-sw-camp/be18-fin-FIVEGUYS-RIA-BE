package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectMetaResponseDto {
  private final Long projectId;
  private final String projectName;

  private final Long clientCompanyId;
  private final String clientCompanyName;

  private final Long clientId;
  private final String clientName;
}
