package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateContractSpaceRequestDto {
    // 계약 수정 시

    private final Long storeContractMapId;

    private final Long storeId;

    private final Long additionalFee;

    private final Long discountAmount;

    private final String description;
}
