package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ContractEstimateDetailResponseDto {
    // 계약용 견적 상세 조회

    private final Long estimateId;

    private final String estimateTitle;

    private final LocalDate estimateDate;

    private final Long clientCompanyId;

    private final String clientCompanyName;

    private final Long clientId;

    private final String clientName;

    private final Estimate.PaymentCondition paymentCondition;

    private final String remark;  // 비고

    private final List<ContractEstimateDetailSpaceResponseDto> spaces;
}
