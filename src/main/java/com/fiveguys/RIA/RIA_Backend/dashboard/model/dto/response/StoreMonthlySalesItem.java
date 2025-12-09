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
public class StoreMonthlySalesItem {

  private Long storeTenantMapId;
  private String storeName;            // 그래프 X축에 쓸 이름
  private BigDecimal totalSalesAmount; // 해당 매장 월 매출
  private long totalSalesCount;        // 해당 매장 월 거래 수
}