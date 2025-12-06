package com.fiveguys.RIA.RIA_Backend.dashboard.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.DashboardMonthContextLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.MonthlyBrandShareLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.MonthlyPerformanceCalculator;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.MonthlyStoreSalesLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.PopupDailySalesLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.TargetMonthContext;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyBrandShareResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyPerformanceResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyStoreSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

  private final DashboardMonthContextLoader monthContextLoader;
  private final MonthlyStoreSalesLoader monthlyStoreSalesLoader;
  private final MonthlyPerformanceCalculator monthlyPerformanceCalculator;
  private final MonthlyBrandShareLoader monthlyBrandShareLoader;
  private final PopupDailySalesLoader popupDailySalesLoader;

  @Override
  public MonthlyStoreSalesResponseDto getMonthlyStoreSales(
      Integer year,
      Integer month,
      Long managerId
  ) {
    TargetMonthContext ctx = monthContextLoader.load(year, month);
    return monthlyStoreSalesLoader.load(ctx, managerId);
  }

  @Override
  public MonthlyPerformanceResponseDto getMonthlyPerformance(
      Integer year,
      Integer month,
      Long managerId
  ) {
    TargetMonthContext ctx = monthContextLoader.load(year, month);
    return monthlyPerformanceCalculator.calculate(ctx, managerId);
  }

  @Override
  public MonthlyBrandShareResponseDto getMonthlyBrandShare(
      Integer year,
      Integer month,
      Long managerId
  ) {
    TargetMonthContext ctx = monthContextLoader.load(year, month);
    return monthlyBrandShareLoader.load(ctx, managerId);
  }

  @Override
  public PopupDailySalesResponseDto getPopupDailySales(
      Integer year,
      Integer month,
      Long managerId
  ) {
    TargetMonthContext ctx = monthContextLoader.load(year, month);
    return popupDailySalesLoader.load(ctx, managerId);
  }
}
