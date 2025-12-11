package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import static java.math.BigDecimal.ZERO;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.BrandMonthlyShareItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyBrandShareResponseDto;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyBrandShareLoader {

  private final SalesMonthlyRepository salesMonthlyRepository;

  public MonthlyBrandShareResponseDto load(TargetMonthContext ctx, Long managerId) {
    if (ctx.isEmpty()) {
      return MonthlyBrandShareResponseDto.builder()
          .year(0)
          .month(0)
          .hasPrevMonth(false)
          .hasNextMonth(false)
          .stores(List.of())
          .build();
    }

    List<Object[]> rows = salesMonthlyRepository.findBrandMonthlySalesByManager(
        ctx.getYear(),
        ctx.getMonth(),
        managerId
    );

    List<BrandMonthlyShareItem> stores = rows.stream()
        .map(row -> {
          Long storeTenantMapId = ((Number) row[0]).longValue();
          String storeName = (String) row[1];
          BigDecimal totalSalesAmount =
              row[2] != null ? (BigDecimal) row[2] : ZERO;

          return BrandMonthlyShareItem.builder()
              .storeTenantMapId(storeTenantMapId)
              .storeName(storeName)
              .totalSalesAmount(totalSalesAmount)
              .build();
        })
        .toList();

    return MonthlyBrandShareResponseDto.builder()
        .year(ctx.getYear())
        .month(ctx.getMonth())
        .hasPrevMonth(ctx.isHasPrev())
        .hasNextMonth(ctx.isHasNext())
        .stores(stores)
        .build();
  }
}
