package com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection;

import java.math.BigDecimal;

public interface MonthlySettlementTrendProjection {
    int getMonth();
    BigDecimal getTotalAmount();
}
