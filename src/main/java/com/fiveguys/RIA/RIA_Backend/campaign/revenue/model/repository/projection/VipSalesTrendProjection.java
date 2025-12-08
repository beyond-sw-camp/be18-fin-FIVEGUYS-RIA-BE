package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;

public interface VipSalesTrendProjection {

  int getYear();

  int getMonth();

  BigDecimal getVipSalesAmount();

  BigDecimal getTotalSalesAmount();
}
