package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;

public interface LatestSettlementProjection {
  Integer getSettlementYear();
  Integer getSettlementMonth();
  BigDecimal getTotalSalesAmount();
  BigDecimal getCommissionRate();
  BigDecimal getCommissionAmount();
  BigDecimal getFinalRevenue();
}
