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
public class BrandMonthlyRankingItem {

  private int rank;               // 랭킹 번호
  private String storeName;       // 브랜드명 (storeDisplayName 등)
  private String floorName;       // 층 정보 (예: B1, 1F, 2F ...)
  private BigDecimal totalAmount; // 해당 월 매출 합계
  private BigDecimal changeRate;  // 전월 대비 증감률 (%)
  private String direction;       // "up" | "down" | "flat"
}
