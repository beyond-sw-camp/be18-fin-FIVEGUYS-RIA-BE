package com.fiveguys.RIA.RIA_Backend.dashboard.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.BrandMonthlyRankingLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.DashboardAuthorization;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.DashboardMonthContextLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.MonthlyBrandShareLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.MonthlyFloorSalesLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.MonthlyPerformanceCalculator;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.MonthlySettlementTrendLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.MonthlyStoreSalesLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.PopupDailySalesLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.StoreAreaEfficiencyLoader;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.component.TargetMonthContext;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.BrandMonthlyRankingResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyBrandShareResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyFloorSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyPerformanceResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlySettlementTrendResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyStoreSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreAreaEfficiencyRankingResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.service.DashboardService;
import java.time.Year;
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
  private final BrandMonthlyRankingLoader  brandMonthlyRankingLoader;
  private final DashboardAuthorization  dashboardAuthorization;
  private final MonthlySettlementTrendLoader  monthlySettlementTrendLoader;
  private final StoreAreaEfficiencyLoader storeAreaEfficiencyLoader;
  private final MonthlyFloorSalesLoader monthlyFloorSalesLoader;

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
  @Override
  public BrandMonthlyRankingResponseDto getBrandMonthlyRanking(
      Integer year,
      Integer month
  ) {
    // 권한 가드
    dashboardAuthorization.ensureSalesLead();

    TargetMonthContext ctx = monthContextLoader.load(year, month);
    return brandMonthlyRankingLoader.load(ctx);
  }

  @Override
  public MonthlySettlementTrendResponseDto getMonthlySettlementTrend(Integer year) {
    dashboardAuthorization.ensureSalesLead();

    int targetYear = (year != null) ? year : Year.now().getValue();
    return monthlySettlementTrendLoader.load(targetYear);
  }

  @Override
  public StoreAreaEfficiencyRankingResponseDto getStoreAreaEfficiencyRanking(
      Integer year,
      Integer month
  ) {
    dashboardAuthorization.ensureSalesLead();

    TargetMonthContext ctx = monthContextLoader.load(year, month);
    return storeAreaEfficiencyLoader.load(ctx);
  }

  @Override
  public MonthlyFloorSalesResponseDto getMonthlyFloorSales(
      Integer year,
      Integer month
  ) {
    dashboardAuthorization.ensureSalesLead();

    TargetMonthContext ctx = monthContextLoader.load(year, month);
    return monthlyFloorSalesLoader.load(ctx);
  }
}
