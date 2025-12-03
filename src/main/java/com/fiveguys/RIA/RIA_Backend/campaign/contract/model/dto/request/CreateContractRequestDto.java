package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateContractRequestDto {

    private final Long estimateId;

    private final Long projectId;

    private final Long pipelineId;

    private final Long clientCompanyId;

    private final Long clientId;

    private final String contractTitle;

    private final Contract.Currency currency;

    private final Long contractAmount;

    private final BigDecimal commissionRate;

    private final Contract.ContractType contractType;

    private final String paymentCondition; // 지급 조건 or 계약 유형

    private final LocalDate contractStartDate;

    private final LocalDate contractEndDate;

    private LocalDate contractDate;

    private final List<ContractSpaceRequestDto> spaces; // 공간 목록
}
