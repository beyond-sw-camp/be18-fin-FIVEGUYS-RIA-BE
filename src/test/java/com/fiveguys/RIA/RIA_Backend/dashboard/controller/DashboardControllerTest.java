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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

  @Mock
  private DashboardService dashboardService;

  @InjectMocks
  private DashboardController dashboardController;

  @Test
  @DisplayName("getMonthlyStoreSales: 로그인 사용자 기준 담당 매장 월 매출 조회 성공")
  void getMonthlyStoreSales_success() {
    // given
    CustomUserDetails loginUser = mock(CustomUserDetails.class);
    Long managerId = 1L;
    given(loginUser.getUserId()).willReturn(managerId);

    Integer year = 2024;
    Integer month = 12;

    MonthlyStoreSalesResponseDto dto = mock(MonthlyStoreSalesResponseDto.class);
    given(dashboardService.getMonthlyStoreSales(year, month, managerId))
        .willReturn(dto);

    // when
    MonthlyStoreSalesResponseDto result =
        dashboardController.getMonthlyStoreSales(loginUser, year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(loginUser).getUserId();
    verify(dashboardService).getMonthlyStoreSales(year, month, managerId);
  }

  @Test
  @DisplayName("getMyMonthlyPerformance: 로그인 사용자 기준 월별 개인 실적 조회 성공")
  void getMyMonthlyPerformance_success() {
    // given
    CustomUserDetails loginUser = mock(CustomUserDetails.class);
    Long managerId = 2L;
    given(loginUser.getUserId()).willReturn(managerId);

    Integer year = 2024;
    Integer month = 11;

    MonthlyPerformanceResponseDto dto = mock(MonthlyPerformanceResponseDto.class);
    given(dashboardService.getMonthlyPerformance(year, month, managerId))
        .willReturn(dto);

    // when
    MonthlyPerformanceResponseDto result =
        dashboardController.getMyMonthlyPerformance(loginUser, year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(loginUser).getUserId();
    verify(dashboardService).getMonthlyPerformance(year, month, managerId);
  }

  @Test
  @DisplayName("getMyBrandMonthlyShare: 로그인 사용자 기준 브랜드별 월 매출 점유율 조회 성공")
  void getMyBrandMonthlyShare_success() {
    // given
    CustomUserDetails loginUser = mock(CustomUserDetails.class);
    Long managerId = 3L;
    given(loginUser.getUserId()).willReturn(managerId);

    Integer year = 2024;
    Integer month = 10;

    MonthlyBrandShareResponseDto dto = mock(MonthlyBrandShareResponseDto.class);
    given(dashboardService.getMonthlyBrandShare(year, month, managerId))
        .willReturn(dto);

    // when
    MonthlyBrandShareResponseDto result =
        dashboardController.getMyBrandMonthlyShare(loginUser, year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(loginUser).getUserId();
    verify(dashboardService).getMonthlyBrandShare(year, month, managerId);
  }

  @Test
  @DisplayName("getMyPopupDailySales: 로그인 사용자 기준 팝업/임시매장 일별 매출 조회 성공")
  void getMyPopupDailySales_success() {
    // given
    CustomUserDetails loginUser = mock(CustomUserDetails.class);
    Long managerId = 4L;
    given(loginUser.getUserId()).willReturn(managerId);

    Integer year = 2024;
    Integer month = 9;

    PopupDailySalesResponseDto dto = mock(PopupDailySalesResponseDto.class);
    given(dashboardService.getPopupDailySales(year, month, managerId))
        .willReturn(dto);

    // when
    PopupDailySalesResponseDto result =
        dashboardController.getMyPopupDailySales(loginUser, year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(loginUser).getUserId();
    verify(dashboardService).getPopupDailySales(year, month, managerId);
  }

  @Test
  @DisplayName("getBrandMonthlyRanking: 팀장용 브랜드별 월 매출 랭킹 조회 성공")
  void getBrandMonthlyRanking_success() {
    // given
    Integer year = 2024;
    Integer month = 8;

    BrandMonthlyRankingResponseDto dto = mock(BrandMonthlyRankingResponseDto.class);
    given(dashboardService.getBrandMonthlyRanking(year, month))
        .willReturn(dto);

    // when
    BrandMonthlyRankingResponseDto result =
        dashboardController.getBrandMonthlyRanking(year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(dashboardService).getBrandMonthlyRanking(year, month);
  }

  @Test
  @DisplayName("getMonthlySettlementTrend: 연도 기준 정산 금액 월별 추이 조회 성공")
  void getMonthlySettlementTrend_success() {
    // given
    Integer year = 2024;

    MonthlySettlementTrendResponseDto dto =
        mock(MonthlySettlementTrendResponseDto.class);

    given(dashboardService.getMonthlySettlementTrend(year))
        .willReturn(dto);

    // when
    MonthlySettlementTrendResponseDto result =
        dashboardController.getMonthlySettlementTrend(year);

    // then
    assertThat(result).isSameAs(dto);
    verify(dashboardService).getMonthlySettlementTrend(year);
  }

  @Test
  @DisplayName("getStoreAreaEfficiencyRanking: 매장 면적 대비 효율 랭킹 조회 성공")
  void getStoreAreaEfficiencyRanking_success() {
    // given
    Integer year = 2024;
    Integer month = 7;

    StoreAreaEfficiencyRankingResponseDto dto =
        mock(StoreAreaEfficiencyRankingResponseDto.class);

    given(dashboardService.getStoreAreaEfficiencyRanking(year, month))
        .willReturn(dto);

    // when
    StoreAreaEfficiencyRankingResponseDto result =
        dashboardController.getStoreAreaEfficiencyRanking(year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(dashboardService).getStoreAreaEfficiencyRanking(year, month);
  }

  @Test
  @DisplayName("getMonthlyFloorSales: 층별 월 매출 조회 성공")
  void getMonthlyFloorSales_success() {
    // given
    Integer year = 2024;
    Integer month = 6;

    MonthlyFloorSalesResponseDto dto = mock(MonthlyFloorSalesResponseDto.class);
    given(dashboardService.getMonthlyFloorSales(year, month))
        .willReturn(dto);

    // when
    MonthlyFloorSalesResponseDto result =
        dashboardController.getMonthlyFloorSales(year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(dashboardService).getMonthlyFloorSales(year, month);
  }
}
