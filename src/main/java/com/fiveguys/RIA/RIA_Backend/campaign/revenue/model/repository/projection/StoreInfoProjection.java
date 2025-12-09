package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection;

import java.math.BigDecimal;

public interface StoreInfoProjection {
  Long getStoreTenantMapId();
  String getFloorName();
  String getStoreNumber();
  String getStoreDisplayName();
  BigDecimal getFinalContractAmount();
}
