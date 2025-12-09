package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;

public interface SettlementAggProjection {
  BigDecimal getTotalSalesAccumulated();
  BigDecimal getCommissionAmountAccumulated();
  BigDecimal getFinalRevenueAccumulated();
}
