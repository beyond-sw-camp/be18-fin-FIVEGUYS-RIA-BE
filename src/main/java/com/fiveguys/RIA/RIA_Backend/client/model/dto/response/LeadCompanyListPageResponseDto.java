package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LeadCompanyListPageResponseDto {
    private long totalCount;
    private int page;
    private int size;
    private List<LeadCompanyListResponseDto> data;
}
