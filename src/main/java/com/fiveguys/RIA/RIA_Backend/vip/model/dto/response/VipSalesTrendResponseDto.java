package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipSalesTrendResponseDto {

  private int baseYear;   // 기준(마지막) 연도
  private int baseMonth;  // 기준(마지막) 월

  // 최근 6개월 월별 VIP 비중
  private List<VipSalesTrendItemResponseDto> items;

  // 최근 6개월 VIP 매출 합계
  private BigDecimal sixMonthVipSales;

  // 최근 6개월 전체 매출 합계
  private BigDecimal sixMonthTotalSales;

  // 전년 동기간 대비 VIP 매출액 증가율 (%)
  private BigDecimal yoyChange;
}
