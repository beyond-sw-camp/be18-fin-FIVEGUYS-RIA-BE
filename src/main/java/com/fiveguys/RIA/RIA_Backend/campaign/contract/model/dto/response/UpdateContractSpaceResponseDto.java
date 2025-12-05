package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class UpdateContractSpaceResponseDto {
    // 계약 수정 시

    private final Long storeContractMapId;

    private final Long rentPrice;

    private final BigDecimal commissionRate;

    private final Long finalContractAmount;

    private final String description;
}
