package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyStoreSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreMonthlySalesItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyStoreSalesLoader {

  private final SalesMonthlyRepository salesMonthlyRepository;

  public MonthlyStoreSalesResponseDto load(TargetMonthContext ctx, Long managerId) {
    if (ctx.isEmpty()) {
      return MonthlyStoreSalesResponseDto.builder()
          .year(0)
          .month(0)
          .hasPrevMonth(false)
          .hasNextMonth(false)
          .stores(List.of())
          .build();
    }

    List<StoreMonthlySalesItem> stores =
        salesMonthlyRepository.findStoreMonthlySalesByManager(
            ctx.getYear(),
            ctx.getMonth(),
            managerId
        );

    return MonthlyStoreSalesResponseDto.builder()
        .year(ctx.getYear())
        .month(ctx.getMonth())
        .hasPrevMonth(ctx.isHasPrev())
        .hasNextMonth(ctx.isHasNext())
        .stores(stores)
        .build();
  }
}
