package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class UpdateContractResponseDto {
    // 계약 수정 시

    private final Long contractId;

    private final String contractTitle;

    private final Contract.PaymentCondition paymentCondition;

    private final Long totalAmount;

    private final String remark;

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    private final List<UpdateContractSpaceResponseDto> spaces;
}
