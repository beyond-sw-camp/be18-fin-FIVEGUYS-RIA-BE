package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContractDeleteResponseDto {
    // 계약 삭제 시

    private final Long contractId;

    private final Contract.Status status;

    private final LocalDateTime updatedAt;
}
