package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EstimateCreateResponseDto {

    private Long estimateId;
    private String title;
    private Long totalPrice;            // 백엔드 계산값
    private String status;              // DRAFT
    private LocalDateTime createdAt;
}
