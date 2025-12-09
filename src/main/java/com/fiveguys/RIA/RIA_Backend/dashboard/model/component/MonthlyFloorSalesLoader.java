package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.FloorMonthlySalesItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyFloorSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.DashboardRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.FloorMonthlySalesProjection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyFloorSalesLoader {

  private final DashboardRepository dashboardRepository;

  public MonthlyFloorSalesResponseDto load(TargetMonthContext ctx) {

    // 컨텍스트 방어
    if (ctx == null
        || ctx.isEmpty()
        || ctx.getYear() <= 0
        || ctx.getMonth() <= 0) {

      return MonthlyFloorSalesResponseDto.builder()
          .year(0)
          .month(0)
          .hasPrevMonth(false)
          .hasNextMonth(false)
          .floors(List.of())
          .build();
    }

    List<FloorMonthlySalesProjection> rows =
        dashboardRepository.findFloorMonthlySales(ctx.getYear(), ctx.getMonth());

    List<FloorMonthlySalesItem> floors = rows.stream()
        .map(r -> FloorMonthlySalesItem.builder()
            .floorName(r.getFloorName())
            .totalAmount(r.getTotalAmount())
            .build())
        .collect(Collectors.toList());

    return MonthlyFloorSalesResponseDto.builder()
        .year(ctx.getYear())
        .month(ctx.getMonth())
        .hasPrevMonth(ctx.isHasPrev())
        .hasNextMonth(ctx.isHasNext())
        .floors(floors)
        .build();
  }
}