package com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection;

import java.math.BigDecimal;

public interface FloorMonthlySalesProjection {

  String getFloorName();

  BigDecimal getTotalAmount();
}
