// src/main/java/com/fiveguys/RIA/RIA_Backend/dashboard/model/repository/projection/StoreAreaEfficiencyProjection.java
package com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection;

import java.math.BigDecimal;

public interface StoreAreaEfficiencyProjection {

  Long getStoreTenantMapId();

  String getStoreName();

  String getFloorName();

  Double getAreaSize();

  BigDecimal getFinalRevenue();
}
