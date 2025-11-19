package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.repository.PipelineRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.PipelineErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PipelineLoader {

  private final PipelineRepository pipelineRepository;

  public Pipeline loadPipelineWithProject(Long pipelineId) {
    return pipelineRepository.findByIdWithProject(pipelineId)
        .orElseThrow(() -> new CustomException(PipelineErrorCode.PIPELINE_NOT_FOUND));
  }
}