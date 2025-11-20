package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectMetaResponseDto {
  private Long projectId;
  private String projectName;

  private Long clientCompanyId;
  private String clientCompanyName;

  private Long clientId;
  private String clientName;
}
