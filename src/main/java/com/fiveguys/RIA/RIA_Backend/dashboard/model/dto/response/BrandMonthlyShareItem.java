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
public class BrandMonthlyShareItem {

  private Long storeTenantMapId;        // 브랜드 ID를 여기 매핑
  private String storeName;             // 브랜드명 (도넛 라벨)
  private BigDecimal totalSalesAmount;  // 브랜드 월 매출 합계
}