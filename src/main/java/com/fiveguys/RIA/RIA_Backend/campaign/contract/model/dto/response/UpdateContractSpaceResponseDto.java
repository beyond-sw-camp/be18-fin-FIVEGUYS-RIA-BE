package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UpdateContractSpaceResponseDto {
    // 계약 수정 시

    private final Long storeContractMapId;

    private final Long rentPrice;

    private final Long additionalFee;

    private final Long discountAmount;

    private final Long finalContractAmount;

    private final String description;
}
