package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectSearchRequestDto {
  private String status;
  private String keyword;
  private String managerName;
}