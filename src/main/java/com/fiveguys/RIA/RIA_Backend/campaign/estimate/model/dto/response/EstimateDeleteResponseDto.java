package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EstimateDeleteResponseDto {
    private Long estimateId;
    private String status;
    private Long deletedBy;
    private LocalDateTime deletedAt;
}