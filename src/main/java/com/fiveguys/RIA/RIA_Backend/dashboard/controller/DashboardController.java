package com.fiveguys.RIA.RIA_Backend.dashboard.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.BrandMonthlyRankingResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyBrandShareResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyFloorSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyPerformanceResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlySettlementTrendResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyStoreSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreAreaEfficiencyRankingResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
  private final DashboardService dashboardService;

  @GetMapping("/sales/monthly")
  public MonthlyStoreSalesResponseDto getMonthlyStoreSales(
      @AuthenticationPrincipal CustomUserDetails loginUser,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month
  ) {
    Long managerId = loginUser.getUserId();
    return dashboardService.getMonthlyStoreSales(year, month, managerId);
  }

  @GetMapping("/sales/performance")
  public MonthlyPerformanceResponseDto getMyMonthlyPerformance(
      @AuthenticationPrincipal CustomUserDetails loginUser,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month
  ) {
    Long managerId = loginUser.getUserId();
    return dashboardService.getMonthlyPerformance(year, month, managerId);
  }
  @GetMapping("/sales/brand")
  public MonthlyBrandShareResponseDto getMyBrandMonthlyShare(
      @AuthenticationPrincipal CustomUserDetails loginUser,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month
  ) {
    Long managerId = loginUser.getUserId();
    return dashboardService.getMonthlyBrandShare(year, month, managerId);
  }

  @GetMapping("/sales/temporary")
  public PopupDailySalesResponseDto getMyPopupDailySales(
      @AuthenticationPrincipal CustomUserDetails loginUser,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month
  ) {
    Long managerId = loginUser.getUserId();
    return dashboardService.getPopupDailySales(year, month, managerId);
  }

  // 팀장용 – 브랜드별 월매출 랭킹
  @GetMapping("/brand/rank")
  public BrandMonthlyRankingResponseDto getBrandMonthlyRanking(
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month
  ) {
    return dashboardService.getBrandMonthlyRanking(year, month);
  }

  // 팀장용 – 월 정산 금액 추이 (연 단위)
  @GetMapping("/sales/trend")
  public MonthlySettlementTrendResponseDto getMonthlySettlementTrend(
      @RequestParam(required = false) Integer year
  ) {
    return dashboardService.getMonthlySettlementTrend(year);
  }

  @GetMapping("/sales/efficiency/rank")
  public StoreAreaEfficiencyRankingResponseDto getStoreAreaEfficiencyRanking(
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month
  ) {
    return dashboardService.getStoreAreaEfficiencyRanking(year, month);
  }

  // 층별 매출
  @GetMapping("/sales/floors")
  public MonthlyFloorSalesResponseDto getMonthlyFloorSales(
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month
  ) {
    return dashboardService.getMonthlyFloorSales(year, month);
  }
}