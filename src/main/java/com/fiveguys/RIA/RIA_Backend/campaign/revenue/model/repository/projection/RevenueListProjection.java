package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RevenueListProjection {

  Long getRevenueId();
  Long getProjectId();
  Long getContractId();
  Long getSettlementId();

  String getContractTitle();
  String getClientCompanyName();

  String getStoreType();

  Long getManagerId();
  String getManagerName();

  Integer getSettlementYear();
  Integer getSettlementMonth();

  BigDecimal getFinalRevenue();

  // 추가
  LocalDate getContractStartDay();
  LocalDate getContractEndDay();
}
