package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;

public interface RevenueDetailProjection {
  Long getRevenueId();
  Long getProjectId();
  String getProjectTitle();
  String getProjectType();
  String getSalesManagerName();

  Long getContractId();
  String getContractTitle();
  String getContractType();
  String getContractStartDate();
  String getContractEndDate();
  String getContractDate();
  BigDecimal getCommissionRate();
  String getPaymentCondition();
  BigDecimal getDepositAmount();
  String getCurrency();

  String getClientCompanyName();
  String getClientName();

  Long getBaseRentSnapshot();
/*  String getRevenueStatus();
  String getRevenueRemark();*/
}
