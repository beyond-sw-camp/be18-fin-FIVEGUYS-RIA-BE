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

    private Long estimateId;        // 견적 ID
    private String estimateNumber;  // 자동 생성된 번호: "EST-20250101-001"
    private int totalSpaces;        // 포함된 공간 개수
    private Long totalAmount;       // 전체 금액합
    private LocalDateTime createdAt;
}
