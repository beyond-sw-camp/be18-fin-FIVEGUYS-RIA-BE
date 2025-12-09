package com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response;

import java.util.List;
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
public class MonthlyStoreSalesResponseDto {

  private int year;    // 기준 연도
  private int month;   // 기준 월

  private boolean hasPrevMonth;  // 이전 달 데이터 존재 여부
  private boolean hasNextMonth;  // 다음 달 데이터 존재 여부

  private List<StoreMonthlySalesItem> stores;  // 매장별 월 매출 리스트
}