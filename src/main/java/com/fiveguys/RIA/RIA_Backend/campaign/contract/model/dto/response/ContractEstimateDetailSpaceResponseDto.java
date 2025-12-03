package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractEstimateDetailSpaceResponseDto {
    // 계약용 견적 상세 조회 시 매장 정보
    
    private final Long storeEstimateMapId;

    private final String storeNumber;

    private final double areaSize;

    private final long rentPrice;

    private final long additionalFee;

    private final long discountAmount;

    private final long finalEstimateAmount;

    private final String description;
}
