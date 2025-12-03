package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ContractPageResponseDto<T> {
    // 계약 목록 조회
    private final int page;

    private final int size;

    private final long totalCount;

    private final List<T> data;
}
