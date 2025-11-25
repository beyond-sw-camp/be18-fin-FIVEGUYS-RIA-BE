package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectManagerUpdateRequestDto {

  @NotNull(message = "새 담당자 ID는 필수입니다.")
  private Long newManagerId;
}