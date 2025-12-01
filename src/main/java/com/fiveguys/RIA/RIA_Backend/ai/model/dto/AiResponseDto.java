package com.fiveguys.RIA.RIA_Backend.ai.model.dto;

import com.fiveguys.RIA.RIA_Backend.ai.model.entity.Ai;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AiResponseDto {
    private Long recoId;
    private String recoType;
    private String targetName;
    private BigDecimal score;
    private String reason;

}
