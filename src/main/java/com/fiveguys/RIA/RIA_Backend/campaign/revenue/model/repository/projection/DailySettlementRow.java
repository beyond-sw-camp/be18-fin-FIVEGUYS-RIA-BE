package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailySettlementRow {

  Long getStoreTenantMapId();

  Long getContractId();

  Long getProjectId();

  LocalDate getSalesDate();

  BigDecimal getTotalSalesAmount();
}