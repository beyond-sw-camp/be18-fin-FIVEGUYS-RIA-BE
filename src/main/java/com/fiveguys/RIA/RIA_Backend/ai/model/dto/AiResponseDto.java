package com.fiveguys.RIA.RIA_Backend.ai.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AiResponseDto {
    private String reason;
}
