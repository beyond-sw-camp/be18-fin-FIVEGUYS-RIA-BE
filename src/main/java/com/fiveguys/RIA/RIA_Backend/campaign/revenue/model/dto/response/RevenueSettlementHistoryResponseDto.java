package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RevenueSettlementHistoryResponseDto {

  private final Long contractId;
  private final int startYear;
  private final int startMonth;
  private final int endYear;
  private final int endMonth;

  private final List<RevenueSettlementHistoryItemResponseDto> settlements;
}