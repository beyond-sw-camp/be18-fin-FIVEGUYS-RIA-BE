package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ContractDetailSpaceResponseDto {
    // 계약 상세 조회

    private final Long storeContractMapId;

    private final Long storeId;

    private final Long floorId;

    private final String storeNumber;

    private final Long finalContractAmount;

    private final LocalDate contractStartDate;

    private final LocalDate contractEndDate;

    private final BigDecimal commissionRate;

    private final String description;
}
