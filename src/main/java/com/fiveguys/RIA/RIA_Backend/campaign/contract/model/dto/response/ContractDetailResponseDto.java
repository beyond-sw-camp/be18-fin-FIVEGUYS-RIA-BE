package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ContractDetailResponseDto {
    // 계약 상세 조회

    private final Long contractId;

    private final Long projectId;

    private final Long pipelineId;

    private final Long clientCompanyId;

    private final String clientCompanyName;

    private final Long clientId;

    private final String clientName;

    private final Long estimateId;

    private final String contractTitle;

    private final BigDecimal commissionRate;

    private final Long totalAmount;

    private final LocalDate contractStartDate;

    private final LocalDate contractEndDate;

    private final LocalDate contractDate;

    private final Contract.PaymentCondition paymentCondition;

    private final Contract.Status status;

    private final Long createUserId;

    private final String createUserName;

    private final LocalDateTime createdAT;

    private final LocalDateTime updatedAt;

    private final List<ContractDetailSpaceResponseDto> spaces;
}
