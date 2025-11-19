package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PipelineStageUpdateRequestDto {

  @NotNull(message = "목표 단계는 필수입니다.")
  private Integer targetStageNo;

  private Boolean forceUpdate = false;
}