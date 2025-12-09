package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import static java.math.BigDecimal.ZERO;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyPerformanceResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreMonthlyPerformanceItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyPerformanceCalculator {

  private final SalesMonthlyRepository salesMonthlyRepository;

  public MonthlyPerformanceResponseDto calculate(TargetMonthContext ctx, Long managerId) {
    if (ctx.isEmpty()) {
      return MonthlyPerformanceResponseDto.builder()
          .year(0)
          .month(0)
          .hasPrevMonth(false)
          .hasNextMonth(false)
          .stores(List.of())
          .build();
    }

    List<Object[]> rows = salesMonthlyRepository.findStoreMonthlyPerformanceByManager(
        ctx.getYear(),
        ctx.getMonth(),
        managerId
    );

    List<StoreMonthlyPerformanceItem> stores = rows.stream()
        .map(this::mapRowToItem)
        .toList();

    return MonthlyPerformanceResponseDto.builder()
        .year(ctx.getYear())
        .month(ctx.getMonth())
        .hasPrevMonth(ctx.isHasPrev())
        .hasNextMonth(ctx.isHasNext())
        .stores(stores)
        .build();
  }

  private StoreMonthlyPerformanceItem mapRowToItem(Object[] row) {
    Long storeTenantMapId = ((Number) row[0]).longValue();
    String storeName = (String) row[1];
    BigDecimal actualAmount = (BigDecimal) row[2];
    BigDecimal expectedRevenue = row[3] != null ? (BigDecimal) row[3] : ZERO;
    BigDecimal marginRateRaw = row[4] != null ? (BigDecimal) row[4] : ZERO;

    BigDecimal marginRate = marginRateRaw
        .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

    if (actualAmount == null) actualAmount = ZERO;

    BigDecimal targetAmount = expectedRevenue;
    BigDecimal expectedProfit = expectedRevenue.multiply(marginRate);
    BigDecimal actualProfit = actualAmount.multiply(marginRate);

    boolean achieved =
        targetAmount.signum() > 0 && actualAmount.compareTo(targetAmount) >= 0;

    return StoreMonthlyPerformanceItem.builder()
        .storeTenantMapId(storeTenantMapId)
        .storeName(storeName)
        .targetAmount(targetAmount.setScale(0, RoundingMode.DOWN))
        .actualAmount(actualAmount.setScale(0, RoundingMode.DOWN))
        .expectedProfit(expectedProfit.setScale(0, RoundingMode.DOWN))
        .actualProfit(actualProfit.setScale(0, RoundingMode.DOWN))
        .achieved(achieved)
        .build();
  }
}
