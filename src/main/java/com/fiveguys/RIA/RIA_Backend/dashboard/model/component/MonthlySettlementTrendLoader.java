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

    List<MonthlySettlementTrendProjection> rows =
        dashboardRepository.findMonthlySettlementTrend(year);

    Map<Integer, BigDecimal> monthlyMap = new HashMap<>();
    for (MonthlySettlementTrendProjection row : rows) {
      BigDecimal amount = Optional
          .ofNullable(row.getTotalAmount())
          .orElse(BigDecimal.ZERO);
      monthlyMap.put(row.getMonth(), amount);
    }

    // 1~12월 전체 채워서 내려준다 (데이터 없으면 0)
    var months = IntStream.rangeClosed(1, 12)
        .mapToObj(m -> MonthlySettlementTrendItem.builder()
            .month(m)
            .totalAmount(monthlyMap.getOrDefault(m, BigDecimal.ZERO))
            .build()
        )
        .toList();

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
