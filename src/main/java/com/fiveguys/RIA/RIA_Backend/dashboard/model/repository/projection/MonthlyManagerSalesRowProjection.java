package com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection;

import java.math.BigDecimal;

public interface MonthlyManagerSalesRowProjection {

  int getSalesYear();

  int getSalesMonth();

  BigDecimal getTotalSalesAmount();
}