package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ContractSummaryDto {

    private final Long contractId;

    private final String contractTitle;

    private final String createdUserName;

    private final String clientCompanyName;

    private final String clientName;

    private final LocalDate contractStartDate;

    private final LocalDate contractEndDate;

    private final Long totalAmount;

    private final BigDecimal commissionRate;

    private final LocalDateTime createdAt;
}
