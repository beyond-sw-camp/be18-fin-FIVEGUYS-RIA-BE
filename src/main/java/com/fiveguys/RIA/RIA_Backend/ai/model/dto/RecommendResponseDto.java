package com.fiveguys.RIA.RIA_Backend.ai.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendResponseDto {
    private boolean success;
    private String message;
}
