package com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StoreMonthlyPerformanceItem {

  private Long storeTenantMapId;   // 그대로 사용
  private String storeName;        // 그대로 사용 (X축)

  private BigDecimal targetAmount;   // 목표 매출
  private BigDecimal actualAmount;   // 실적 매출
  private BigDecimal expectedProfit; // 목표 이익
  private BigDecimal actualProfit; // 실제 이익
  private boolean achieved;          // 실적 >= 목표
}