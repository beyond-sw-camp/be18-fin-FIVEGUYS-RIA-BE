package com.fiveguys.RIA.RIA_Backend.dashboard.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyBrandShareResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyPerformanceResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyStoreSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.service.DashboardService;
import java.util.List;
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
}