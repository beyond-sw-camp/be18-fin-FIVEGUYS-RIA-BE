package com.fiveguys.RIA.RIA_Backend.dashboard.model.service;

import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.BrandMonthlyRankingResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyBrandShareResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyFloorSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyPerformanceResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlySettlementTrendResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyStoreSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreAreaEfficiencyRankingResponseDto;

public interface DashboardService {

  MonthlyStoreSalesResponseDto getMonthlyStoreSales(
      Integer year,
      Integer month,
      Long managerId
  );

  MonthlyPerformanceResponseDto getMonthlyPerformance(
      Integer year,
      Integer month,
      Long managerId
  );

  MonthlyBrandShareResponseDto getMonthlyBrandShare(
      Integer year,
      Integer month,
      Long managerId
  );

  PopupDailySalesResponseDto getPopupDailySales(
      Integer year,
      Integer month,
      Long managerId
  );

  BrandMonthlyRankingResponseDto getBrandMonthlyRanking(
      Integer year,
      Integer month
  );

  MonthlySettlementTrendResponseDto getMonthlySettlementTrend(
      Integer year);

  StoreAreaEfficiencyRankingResponseDto getStoreAreaEfficiencyRanking(
      Integer year,
      Integer month
  );

  MonthlyFloorSalesResponseDto getMonthlyFloorSales(
      Integer year,
      Integer month
  );
}