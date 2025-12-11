package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.*;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.DashboardRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.MonthlySettlementTrendProjection;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MonthlySettlementTrendLoader {

  private final DashboardRepository dashboardRepository;

  @Transactional(readOnly = true)
  public MonthlySettlementTrendResponseDto load(int year) {

    // 1) 조회 결과 널 방어
    List<MonthlySettlementTrendProjection> rows =
        Optional.ofNullable(dashboardRepository.findMonthlySettlementTrend(year))
            .orElseGet(List::of);

    Map<Integer, BigDecimal> monthlyMap = new HashMap<>();

    // 2) projection 널 / month 널 / amount 널 방어
    for (MonthlySettlementTrendProjection row : rows) {
      if (row == null) {
        continue;
      }

      int monthValue = row.getMonth();

      // 월이 1~12 범위가 아니면 무시
      if (monthValue < 1 || monthValue > 12) {
        continue;
      }

      BigDecimal amount = Optional.ofNullable(row.getTotalAmount())
          .orElse(BigDecimal.ZERO);

      monthlyMap.put(monthValue, amount);
    }
    // 3) 1~12월 전체 채워서 내려준다 (데이터 없으면 0)
    List<MonthlySettlementTrendItem> months = IntStream.rangeClosed(1, 12)
        .mapToObj(m -> MonthlySettlementTrendItem.builder()
            .month(m)
            .totalAmount(monthlyMap.getOrDefault(m, BigDecimal.ZERO))
            .build()
        )
        .toList();

    // 4) 이전/다음 연도 존재 여부 (데이터 없어도 false 로만 떨어짐)
    boolean hasPrevYear = dashboardRepository.existsBySettlementYear(year - 1);
    boolean hasNextYear = dashboardRepository.existsBySettlementYear(year + 1);

    return MonthlySettlementTrendResponseDto.builder()
        .year(year)
        .hasPrevYear(hasPrevYear)
        .hasNextYear(hasNextYear)
        .items(months)
        .build();
  }
}