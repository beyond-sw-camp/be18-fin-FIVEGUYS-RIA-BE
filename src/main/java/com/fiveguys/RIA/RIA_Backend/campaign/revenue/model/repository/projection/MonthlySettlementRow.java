package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;

public interface MonthlySettlementRow {

  Long getStoreTenantMapId();   // STORE_TENANT_MAP_ID
  Long getContractId();         // CONTRACT_ID
  Long getProjectId();          // PROJECT_ID

  BigDecimal getTotalSalesAmount(); // SALES_MONTHLY.TOTAL_SALES_AMOUNT
  BigDecimal getRentPrice();        // STORE_CONTRACT_MAP.RENT_PRICE
  BigDecimal getCommissionRate();   // STORE_CONTRACT_MAP.COMMISSION_RATE
}