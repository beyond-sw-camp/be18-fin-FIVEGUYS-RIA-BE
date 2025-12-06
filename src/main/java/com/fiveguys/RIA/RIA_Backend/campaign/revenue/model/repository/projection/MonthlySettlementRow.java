package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;

public interface MonthlySettlementRow {

  Long getStoreTenantMapId();

  Long getContractId();

  Long getProjectId();

  BigDecimal getTotalSalesAmount();
}