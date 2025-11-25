package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;

public interface EstimateService {

    EstimateCreateResponseDto createEstimate(EstimateCreateRequestDto dto);
}
