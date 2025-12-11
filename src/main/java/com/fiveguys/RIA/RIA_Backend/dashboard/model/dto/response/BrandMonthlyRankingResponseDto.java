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
public class BrandMonthlyRankingResponseDto {

  private int year;
  private int month;

  private boolean hasPrevMonth;
  private boolean hasNextMonth;

  private List<BrandMonthlyRankingItem> topBrands;     // 상위 TOP5
  private List<BrandMonthlyRankingItem> bottomBrands;  // 하위 TOP5
}
