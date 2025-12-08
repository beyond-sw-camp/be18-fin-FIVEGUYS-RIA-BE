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
public class MonthlyPerformanceResponseDto {

  private int year;
  private int month;

  private boolean hasPrevMonth;
  private boolean hasNextMonth;

  private List<StoreMonthlyPerformanceItem> stores;   // ← 핵심, 1번과 동일한 리스트 구조
}