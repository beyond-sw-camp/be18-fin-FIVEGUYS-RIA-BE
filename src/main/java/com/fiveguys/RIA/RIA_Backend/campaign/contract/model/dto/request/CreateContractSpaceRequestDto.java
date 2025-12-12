package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateContractSpaceRequestDto {
    // 계약 생성 시
    
    private final Long storeId;

    private final Long additionalFee;

    private final Long discountAmount;

    private final String description;
}
