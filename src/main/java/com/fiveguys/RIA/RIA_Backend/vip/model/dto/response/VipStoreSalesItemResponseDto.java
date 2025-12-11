package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipStoreSalesItemResponseDto {

  private String storeName;          // 매장명
  private BigDecimal vipRatio;       // VIP 비중(%)
  private BigDecimal vipSalesAmount; // VIP 매출액
  private BigDecimal totalSalesAmount; // 총 매출액
  private BigDecimal momChange;      // 전월 대비 비중 변화(%포인트)
}
