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
public class FloorMonthlySalesItem {

  private String floorName;      // 층 이름 (B1, 1F, 2F ...)
  private BigDecimal totalAmount; // 해당 월 매출 합계
}
