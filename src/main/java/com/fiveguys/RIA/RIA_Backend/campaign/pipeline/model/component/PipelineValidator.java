package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.PipelineErrorCode;
import org.springframework.stereotype.Component;

@Component
public class PipelineValidator {

  public void validateTargetStage(Pipeline pipeline, int targetStage) {
    if (pipeline.getCurrentStage() == targetStage) {
      throw new CustomException(PipelineErrorCode.ALREADY_IN_STAGE);
    }

    if (targetStage < 1 || targetStage > 5) {
      throw new CustomException(PipelineErrorCode.INVALID_STAGE);
    }
  }
}
