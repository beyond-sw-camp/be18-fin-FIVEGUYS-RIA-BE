package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ContractEstimateResponseDto {
    // 계약용 견적 목록 조회

    private final Long estimateId;

    private final String estimateTitle;

    private final String clientCompanyName;

    private final String clientName;

    private final LocalDate estimateDate;

    private final Long totalAmount;

    private final Estimate.Status status;
}
