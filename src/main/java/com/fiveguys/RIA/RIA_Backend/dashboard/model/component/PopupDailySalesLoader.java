package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesDailyRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesResponseDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopupDailySalesLoader {

  private final SalesDailyRepository salesDailyRepository;

  public PopupDailySalesResponseDto load(TargetMonthContext ctx, Long managerId) {

    // 컨텍스트 방어: null, empty, 0, null 값 모두 빈 응답
    if (ctx == null
        || ctx.isEmpty()
        || ctx.getYear() <= 0
        || ctx.getMonth() <= 0) {

      return PopupDailySalesResponseDto.builder()
          .year(0)
          .month(0)
          .hasPrevMonth(false)
          .hasNextMonth(false)
          .days(List.of())
          .build();
    }

    YearMonth yearMonth = YearMonth.of(ctx.getYear(), ctx.getMonth());
    LocalDate startDate = yearMonth.atDay(1);
    LocalDate endDate = yearMonth.atEndOfMonth();

    List<Object[]> rows =
        salesDailyRepository.findPopupDailySalesByManagerAndPeriod(
            startDate, endDate, managerId
        );

    List<PopupDailySalesItem> days = rows.stream()
        .map(row -> {
          LocalDate salesDate = (LocalDate) row[0];

          Long storeTenantMapId =
              row[1] == null ? null : ((Number) row[1]).longValue();

          String storeName = (String) row[2];

          BigDecimal totalAmount =
              row[3] == null ? BigDecimal.ZERO : (BigDecimal) row[3];

          long totalCount =
              row[4] == null ? 0L : ((Number) row[4]).longValue();

          return PopupDailySalesItem.builder()
              .day(salesDate.getDayOfMonth())
              .storeTenantMapId(storeTenantMapId)
              .storeName(storeName)
              .totalAmount(totalAmount)
              .totalCount(totalCount)
              .build();
        })
        .toList();

    return PopupDailySalesResponseDto.builder()
        .year(ctx.getYear())
        .month(ctx.getMonth())
        .hasPrevMonth(ctx.isHasPrev())
        .hasNextMonth(ctx.isHasNext())
        .days(days)
        .build();
  }
}
