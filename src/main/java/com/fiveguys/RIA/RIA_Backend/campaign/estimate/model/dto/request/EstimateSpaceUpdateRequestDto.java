package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EstimateSpaceUpdateRequestDto {

    private Long storeEstimateMapId;

    private Long additionalFee;
    private Long discountAmount;
    private String description;

}