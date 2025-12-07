package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;

public interface VipStoreMonthlyProjection {

  Long getStoreTenantMapId();

  String getStoreName();

  BigDecimal getVipSalesAmount();

  BigDecimal getTotalSalesAmount();
}
