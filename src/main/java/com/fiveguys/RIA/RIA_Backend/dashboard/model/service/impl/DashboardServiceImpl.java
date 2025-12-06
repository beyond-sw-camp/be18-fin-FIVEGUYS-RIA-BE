package com.fiveguys.RIA.RIA_Backend.dashboard.model.service.impl;

import static java.math.BigDecimal.ZERO;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesDailyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.BrandMonthlyShareItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyBrandShareResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyPerformanceResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.MonthlyStoreSalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.PopupDailySalesResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreMonthlyPerformanceItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreMonthlySalesItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.service.DashboardService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

  private final SalesMonthlyRepository salesMonthlyRepository;
  private final SalesDailyRepository salesDailyRepository;

  @Override
  public MonthlyStoreSalesResponseDto getMonthlyStoreSales(
      Integer year,
      Integer month,
      Long managerId
  ) {
    int targetYear;
    int targetMonth;

    // 요청 year/month가 없으면 sales_monthly에서 가장 최신 연월 사용
    if (year == null || month == null) {
      List<Object[]> ymList = salesMonthlyRepository.findAllYearMonthOrderByLatest();
      if (ymList.isEmpty()) {
        return MonthlyStoreSalesResponseDto.builder()
            .year(0)
            .month(0)
            .hasPrevMonth(false)
            .hasNextMonth(false)
            .stores(List.of())
            .build();
      }
      Object[] first = ymList.get(0);
      targetYear = ((Number) first[0]).intValue();
      targetMonth = ((Number) first[1]).intValue();
    } else {
      targetYear = year;
      targetMonth = month;
    }

    int ym = targetYear * 100 + targetMonth;

    List<StoreMonthlySalesItem> stores =
        salesMonthlyRepository.findStoreMonthlySalesByManager(
            targetYear, targetMonth, managerId);

    boolean hasPrev = salesMonthlyRepository.existsBefore(ym);
    boolean hasNext = salesMonthlyRepository.existsAfter(ym);

    return MonthlyStoreSalesResponseDto.builder()
        .year(targetYear)
        .month(targetMonth)
        .hasPrevMonth(hasPrev)
        .hasNextMonth(hasNext)
        .stores(stores)
        .build();
  }

  // 2번 그래프: 입점(RENTAL) 기준 단일 월 목표 vs 실적
  @Override
  public MonthlyPerformanceResponseDto getMonthlyPerformance(
      Integer year,
      Integer month,
      Long managerId
  ) {
    int targetYear;
    int targetMonth;

    // year/month 없으면 RENTAL 기준 가장 최신 연월
    if (year == null || month == null) {
      List<Object[]> ymList = salesMonthlyRepository.findAllYearMonthOrderByLatest();
      if (ymList.isEmpty()) {
        return MonthlyPerformanceResponseDto.builder()
            .year(0)
            .month(0)
            .hasPrevMonth(false)
            .hasNextMonth(false)
            .stores(List.of())
            .build();
      }
      Object[] first = ymList.get(0);
      targetYear = ((Number) first[0]).intValue();
      targetMonth = ((Number) first[1]).intValue();
    } else {
      targetYear = year;
      targetMonth = month;
    }

    int ym = targetYear * 100 + targetMonth;

    boolean hasPrev = salesMonthlyRepository.existsBefore(ym);
    boolean hasNext = salesMonthlyRepository.existsAfter(ym);

    // 매장별 실적 + 목표 조회
    List<Object[]> rows = salesMonthlyRepository.findStoreMonthlyPerformanceByManager(
        targetYear,
        targetMonth,
        managerId
    );

    List<StoreMonthlyPerformanceItem> stores = rows.stream()
        .map(row -> {

          Long storeTenantMapId = ((Number) row[0]).longValue();
          String storeName = (String) row[1];
          BigDecimal actualAmount = (BigDecimal) row[2];
          BigDecimal expectedRevenue =
              row[3] != null ? (BigDecimal) row[3] : ZERO;

          BigDecimal marginRateRaw =
              row[4] != null ? (BigDecimal) row[4] : ZERO;

          // DB에는 10, 15 등으로 저장 → 계산 시 0.10, 0.15로 변환
          BigDecimal marginRate = marginRateRaw
              .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

          if (actualAmount == null)
            actualAmount = ZERO;

          BigDecimal targetAmount = expectedRevenue;
          BigDecimal expectedProfit = expectedRevenue.multiply(marginRate);
          BigDecimal actualProfit = actualAmount.multiply(marginRate);

          boolean achieved =
              targetAmount.signum() > 0 && actualAmount.compareTo(targetAmount) >= 0;

          return StoreMonthlyPerformanceItem.builder()
              .storeTenantMapId(storeTenantMapId)
              .storeName(storeName)
              .targetAmount(targetAmount.setScale(0, RoundingMode.DOWN))
              .actualAmount(actualAmount.setScale(0, RoundingMode.DOWN))
              .expectedProfit(expectedProfit.setScale(0, RoundingMode.DOWN))
              .actualProfit(actualProfit.setScale(0, RoundingMode.DOWN))
              .achieved(achieved)
              .build();
        })
        .toList();

    return MonthlyPerformanceResponseDto.builder()
        .year(targetYear)
        .month(targetMonth)
        .hasPrevMonth(hasPrev)
        .hasNextMonth(hasNext)
        .stores(stores)
        .build();
  }

  @Override
  public MonthlyBrandShareResponseDto getMonthlyBrandShare(
      Integer year,
      Integer month,
      Long managerId
  ) {
    int targetYear;
    int targetMonth;

    if (year == null || month == null) {
      List<Object[]> ymList = salesMonthlyRepository.findAllYearMonthOrderByLatest();
      if (ymList.isEmpty()) {
        return MonthlyBrandShareResponseDto.builder()
            .year(0)
            .month(0)
            .hasPrevMonth(false)
            .hasNextMonth(false)
            .stores(List.of())
            .build();
      }
      Object[] first = ymList.get(0);
      targetYear = ((Number) first[0]).intValue();
      targetMonth = ((Number) first[1]).intValue();
    } else {
      targetYear = year;
      targetMonth = month;
    }

    int ym = targetYear * 100 + targetMonth;

    boolean hasPrev = salesMonthlyRepository.existsBefore(ym);
    boolean hasNext = salesMonthlyRepository.existsAfter(ym);

    List<Object[]> rows = salesMonthlyRepository.findBrandMonthlySalesByManager(
        targetYear,
        targetMonth,
        managerId
    );

    List<BrandMonthlyShareItem> stores = rows.stream()
        .map(row -> {
          Long storeTenantMapId = ((Number) row[0]).longValue();
          String storeName = (String) row[1];
          BigDecimal totalSalesAmount = row[2] != null ? (BigDecimal) row[2] : ZERO;

          return BrandMonthlyShareItem.builder()
              .storeTenantMapId(storeTenantMapId)
              .storeName(storeName)
              .totalSalesAmount(totalSalesAmount)
              .build();
        })
        .toList();

    return MonthlyBrandShareResponseDto.builder()
        .year(targetYear)
        .month(targetMonth)
        .hasPrevMonth(hasPrev)
        .hasNextMonth(hasNext)
        .stores(stores)
        .build();
  }

  @Override
  public PopupDailySalesResponseDto getPopupDailySales(
      Integer year,
      Integer month,
      Long managerId
  ) {
    int targetYear;
    int targetMonth;

    if (year == null || month == null) {
      List<Object[]> ymList = salesMonthlyRepository.findAllYearMonthOrderByLatest();
      if (ymList.isEmpty()) {
        return PopupDailySalesResponseDto.builder()
            .year(0)
            .month(0)
            .hasPrevMonth(false)
            .hasNextMonth(false)
            .days(List.of())
            .build();
      }
      Object[] first = ymList.get(0);
      targetYear = ((Number) first[0]).intValue();
      targetMonth = ((Number) first[1]).intValue();
    } else {
      targetYear = year;
      targetMonth = month;
    }

    int ym = targetYear * 100 + targetMonth;

    YearMonth yearMonth = YearMonth.of(targetYear, targetMonth);
    LocalDate startDate = yearMonth.atDay(1);
    LocalDate endDate = yearMonth.atEndOfMonth();

    boolean hasPrev = salesMonthlyRepository.existsBefore(ym);
    boolean hasNext = salesMonthlyRepository.existsAfter(ym);

    List<Object[]> rows =
        salesDailyRepository.findPopupDailySalesByManagerAndPeriod(
            startDate, endDate, managerId
        );

    List<PopupDailySalesItem> days = rows.stream()
        .map(row -> {
          LocalDate salesDate = (LocalDate) row[0];
          Long storeTenantMapId = ((Number) row[1]).longValue();
          String storeName = (String) row[2];
          BigDecimal totalAmount = (BigDecimal) row[3];
          long totalCount = ((Number) row[4]).longValue();

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
        .year(targetYear)
        .month(targetMonth)
        .hasPrevMonth(hasPrev)
        .hasNextMonth(hasNext)
        .days(days)
        .build();
  }
}