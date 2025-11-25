package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EstimateCreateRequestDto {

    // 연관 ID
    private Long projectId;        // 옵션
    private Long pipelineId;       // 옵션
    private Long createdUserId;    // 필수 (작성자)
    private Long clientId;         // 필수
    private Long clientCompanyId;  // 필수
    private Long storeId;          // 필수

    // 기본 정보
    private String title;          // 견적 제목

    // 금액
    private Long basePrice;
    private Long additionalPrice;
    private Long discountPrice;

    // 날짜
    private LocalDate estimateDate;   // 견적일
    private LocalDate deliveryDate;   // 납기일

    // 결제 조건
    private String paymentCondition;  // "CASH" / "CARD"

    // 비고
    private String remark;
}
