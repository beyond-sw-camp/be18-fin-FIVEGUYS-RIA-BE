package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.RevenueSettlementCalculator;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueSettlementRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.DailySettlementRow;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.MonthlySettlementRow;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SettlementService;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap.StoreType;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementServiceImpl implements SettlementService {

  private final RevenueSettlementRepository revenueSettlementRepository;
  private final RevenueSettlementCalculator revenueSettlementCalculator;

  /**
   * 상설(정규) 매장 월 정산
   */
  @Override
  public void settleMonthlyForRegular(int year, int month) {

    YearMonth ym = YearMonth.of(year, month);
    LocalDate startOfMonth = ym.atDay(1);
    LocalDate endOfMonth   = ym.atEndOfMonth();

    // REGULAR 매장만 대상으로 월 정산 원천 데이터 조회
    List<MonthlySettlementRow> rows =
        revenueSettlementRepository.findMonthlySettlementRowsByStoreType(
            year,
            month,
            startOfMonth,
            endOfMonth,
            StoreType.REGULAR
        );

    // 월 정산 계산 및 REVENUE_SETTLEMENT 반영
    revenueSettlementCalculator.settleMonth(year, month, rows);
  }

  @Override
  public void settleLastMonthForRegular() {
    LocalDate base = LocalDate.now().minusMonths(1);
    settleMonthlyForRegular(base.getYear(), base.getMonthValue());
  }

  /**
   * 임시(팝업/전시) 매장 일 정산
   */
  @Override
  public void settleDailyForTemporary(LocalDate targetDate) {

    // POPUP / EXHIBITION 매장만 대상으로 일 정산 원천 데이터 조회
    List<DailySettlementRow> rows =
        revenueSettlementRepository.findDailySettlementRowsByStoreTypes(
            targetDate,
            List.of(StoreType.POPUP, StoreType.EXHIBITION)
        );

    // 일 정산 계산 및 REVENUE_SETTLEMENT 반영
    revenueSettlementCalculator.settleDay(targetDate, rows);
  }

  @Override
  public void settleYesterdayForTemporary() {
    LocalDate targetDate = LocalDate.now().minusDays(1);
    settleDailyForTemporary(targetDate);
  }
}