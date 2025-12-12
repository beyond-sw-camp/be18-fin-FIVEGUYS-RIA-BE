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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "대시보드", description = "영업/팀장 대시보드 지표 조회 API")
public class DashboardController {

  private final DashboardService dashboardService;

  @Operation(
      summary = "담당 매장의 월 매출 조회",
      description = "로그인한 영업 담당자의 매장 기준으로 월 매출 정보를 조회한다."
  )
  @GetMapping("/sales/monthly")
  public MonthlyStoreSalesResponseDto getMonthlyStoreSales(
      @AuthenticationPrincipal CustomUserDetails loginUser,
      @Parameter(description = "연도", example = "2024") @RequestParam(required = false) Integer year,
      @Parameter(description = "월", example = "12") @RequestParam(required = false) Integer month
  ) {
    Long managerId = loginUser.getUserId();
    return dashboardService.getMonthlyStoreSales(year, month, managerId);
  }

  @Operation(
      summary = "담당자의 월별 실적(매출·계약·기여도) 조회",
      description = "개인 실적 대시보드에 필요한 월 기준 실적 데이터를 조회한다."
  )
  @GetMapping("/sales/performance")
  public MonthlyPerformanceResponseDto getMyMonthlyPerformance(
      @AuthenticationPrincipal CustomUserDetails loginUser,
      @Parameter(description = "연도", example = "2024") @RequestParam(required = false) Integer year,
      @Parameter(description = "월", example = "12") @RequestParam(required = false) Integer month
  ) {
    Long managerId = loginUser.getUserId();
    return dashboardService.getMonthlyPerformance(year, month, managerId);
  }

  @Operation(
      summary = "브랜드별 월 매출 점유율 조회",
      description = "로그인한 담당자의 매장/브랜드 기준 월 매출 점유율을 조회한다."
  )
  @GetMapping("/sales/brand")
  public MonthlyBrandShareResponseDto getMyBrandMonthlyShare(
      @AuthenticationPrincipal CustomUserDetails loginUser,
      @Parameter(description = "연도", example = "2024") @RequestParam(required = false) Integer year,
      @Parameter(description = "월", example = "12") @RequestParam(required = false) Integer month
  ) {
    Long managerId = loginUser.getUserId();
    return dashboardService.getMonthlyBrandShare(year, month, managerId);
  }

  @Operation(
      summary = "팝업/임시매장 일별 매출 조회",
      description = "영업 담당자가 맡은 팝업 매장의 월간 일별 매출 데이터를 조회한다."
  )
  @GetMapping("/sales/temporary")
  public PopupDailySalesResponseDto getMyPopupDailySales(
      @AuthenticationPrincipal CustomUserDetails loginUser,
      @Parameter(description = "연도", example = "2024") @RequestParam(required = false) Integer year,
      @Parameter(description = "월", example = "12") @RequestParam(required = false) Integer month
  ) {
    Long managerId = loginUser.getUserId();
    return dashboardService.getPopupDailySales(year, month, managerId);
  }

  @Operation(
      summary = "브랜드별 월매출 랭킹 조회 (팀장 전용)",
      description = "팀장이 팀 전체 브랜드 기준 월별 매출 랭킹을 조회한다."
  )
  @GetMapping("/brand/rank")
  public BrandMonthlyRankingResponseDto getBrandMonthlyRanking(
      @Parameter(description = "연도", example = "2024") @RequestParam(required = false) Integer year,
      @Parameter(description = "월", example = "12") @RequestParam(required = false) Integer month
  ) {
    return dashboardService.getBrandMonthlyRanking(year, month);
  }

  @Operation(
      summary = "정산 금액 월별 추이 조회 (연 단위, 팀장 전용)",
      description = "특정 연도 기준 월별 정산금액 추세를 조회한다."
  )
  @GetMapping("/sales/trend")
  public MonthlySettlementTrendResponseDto getMonthlySettlementTrend(
      @Parameter(description = "연도", example = "2024") @RequestParam(required = false) Integer year
  ) {
    return dashboardService.getMonthlySettlementTrend(year);
  }

  @Operation(
      summary = "매장 면적 대비 효율 랭킹 조회 (TOP5 / BOTTOM5)",
      description = "특정 연·월 기준 매장 효율(매출/면적) 상위·하위 랭킹을 조회한다."
  )
  @GetMapping("/sales/efficiency/rank")
  public StoreAreaEfficiencyRankingResponseDto getStoreAreaEfficiencyRanking(
      @Parameter(description = "연도", example = "2024") @RequestParam(required = false) Integer year,
      @Parameter(description = "월", example = "12") @RequestParam(required = false) Integer month
  ) {
    return dashboardService.getStoreAreaEfficiencyRanking(year, month);
  }

  @Operation(
      summary = "층별 월 매출 조회",
      description = "건물 층 단위 매출 합계를 월 기준으로 조회한다."
  )
  @GetMapping("/sales/floors")
  public MonthlyFloorSalesResponseDto getMonthlyFloorSales(
      @Parameter(description = "연도", example = "2024") @RequestParam(required = false) Integer year,
      @Parameter(description = "월", example = "12") @RequestParam(required = false) Integer month
  ) {
    return dashboardService.getMonthlyFloorSales(year, month);
  }
}
