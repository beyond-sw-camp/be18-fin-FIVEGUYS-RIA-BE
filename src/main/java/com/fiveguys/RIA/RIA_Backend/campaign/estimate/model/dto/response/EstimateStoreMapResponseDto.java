package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EstimateStoreMapResponseDto {
    private Long storeId;
    private String floorName;
    private String storeName;

    private Long baseAmount;
    private Long additionalFee;
    private Long discountAmount;
    private Long finalAmount;

    private String remark;
}
