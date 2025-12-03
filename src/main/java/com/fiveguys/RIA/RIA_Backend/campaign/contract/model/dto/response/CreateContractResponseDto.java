package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class CreateContractResponseDto {
    // 계약 생성 시
    private final Long contractId;

    private final Long totalAmount;

    private final Contract.Status status;

    private final Long createUserId;

    private final LocalDate contractStartDate;

    private final LocalDate contractEndDate;

    private final LocalDateTime createdAt;
}
