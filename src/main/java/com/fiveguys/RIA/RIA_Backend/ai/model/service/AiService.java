package com.fiveguys.RIA.RIA_Backend.ai.model.service;

import com.fiveguys.RIA.RIA_Backend.ai.model.dto.AiResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.dto.RecommendResponseDto;

import java.util.List;

public interface AiService {

    RecommendResponseDto RecommendationsVip(Long vipId);

    List<AiResponseDto> getRecommendations(Long vipId);
}
