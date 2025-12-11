package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipStatsResponseDto {

  private int year;
  private int month;

  // 이번 달
  private BigDecimal vipRatio;          // VIP 매출 비중 (%)
  private BigDecimal vipSalesAmount;    // VIP 매출
  private BigDecimal totalSalesAmount;  // 전체 매출

  // 전월
  private BigDecimal prevVipRatio;      // 전월 VIP 비중 (%)

  // 전월 대비
  private BigDecimal diffRatio;         // vipRatio - prevVipRatio (단위: %p)
  private String direction;             // "up" | "down" | "flat"
}
