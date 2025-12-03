package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ContractSpaceRequestDto {

    private final Long storeId;

    private final Long additionalFee;

    private final Long discountAmount;

    private final LocalDate contractStartDate;

    private final LocalDate contractEndDate;

    private final BigDecimal commissionRate;

    private final String description;
}
