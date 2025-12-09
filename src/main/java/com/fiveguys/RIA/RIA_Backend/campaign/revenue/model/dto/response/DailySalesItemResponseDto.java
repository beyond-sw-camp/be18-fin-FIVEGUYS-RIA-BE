package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailySalesItemResponseDto {

  private LocalDate salesDate;          // 매출 일자
  private BigDecimal totalSalesAmount;  // 총 매출 금액
  private int totalSalesCount;          // 총 거래 건수
  private BigDecimal vipSalesAmount;    // VIP 매출 금액
  private int vipSalesCount;            // VIP 거래 건수
}