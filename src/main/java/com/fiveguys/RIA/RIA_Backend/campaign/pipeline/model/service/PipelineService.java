package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.service;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.request.PipelineStageUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageUpdateResponseDto;

public interface PipelineService {

  PipelineStageUpdateResponseDto updateStage(
      Long pipelineId,
      PipelineStageUpdateRequestDto dto,
      CustomUserDetails user
  );
}
