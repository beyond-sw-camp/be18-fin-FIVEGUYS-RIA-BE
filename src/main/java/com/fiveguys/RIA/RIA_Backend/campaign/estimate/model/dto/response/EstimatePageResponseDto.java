package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EstimatePageResponseDto<T> {
    private int page;
    private int size;
    private long totalCount;
    private List<T> data;
}
