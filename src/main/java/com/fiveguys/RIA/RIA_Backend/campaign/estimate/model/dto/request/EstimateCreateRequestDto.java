package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EstimateCreateRequestDto {

    // 연관 ID
    private Long projectId;         // 옵션
    private Long pipelineId;        // 옵션
    private Long createdUserId;     // 필수
    private Long clientId;          // 필수
    private Long clientCompanyId;   // 필수
    private Long proposalId;        // 옵션
    // 기본 정보
    private String title;               // 견적 제목
    private LocalDate estimateDate;     // 견적일
    private LocalDate deliveryDate;     // 납기일
    private String paymentCondition;    // 선불/후불
    private String remark;              // 비고

    // 공간 목록 (필수)
    private List<EstimateSpaceRequestDto> spaces;
}
