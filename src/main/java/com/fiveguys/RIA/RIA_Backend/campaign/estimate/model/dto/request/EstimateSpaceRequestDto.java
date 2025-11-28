package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EstimateSpaceRequestDto {
    private Long storeId;
    private Long additionalFee;
    private Long discountAmount;
    private String description;
}

