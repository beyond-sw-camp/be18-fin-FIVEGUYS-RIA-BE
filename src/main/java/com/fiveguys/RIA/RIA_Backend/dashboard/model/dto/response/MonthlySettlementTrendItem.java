package com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MonthlySettlementTrendItem {

  private int month;               // 1 ~ 12
  private BigDecimal totalAmount;  // 해당 월 정산 금액 합계 (FINAL_REVENUE 합)
}
