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
public class StoreAreaEfficiencyItem {

  private int rank;                 // 랭킹 (1~5)
  private Long storeTenantMapId;    // 매장-입점 매핑 ID
  private String storeName;         // 매장명 (storeDisplayName)
  private String floorName;         // 층 (B1, 1F, 2F ...)

  private BigDecimal areaSize;      // 면적 m²
  private BigDecimal finalRevenue;  // 해당 월 실제 수익 합계 (FINAL_REVENUE)
  private BigDecimal efficiency;    // 면적당 수익 = finalRevenue / areaSize (원/㎡)
}
