package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RevenueSettlementHistoryItemResponseDto {

  private final int settlementYear;
  private final int settlementMonth;
  private final BigDecimal totalSalesAmount;
  private final BigDecimal commissionRate;
  private final BigDecimal commissionAmount;
  private final BigDecimal finalRevenue;
}