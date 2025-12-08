package com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MonthlySettlementTrendResponseDto {

  private int year;

  private boolean hasPrevYear;
  private boolean hasNextYear;

  // 1~12월 라인차트용 데이터
  private List<MonthlySettlementTrendItem> items;
}
