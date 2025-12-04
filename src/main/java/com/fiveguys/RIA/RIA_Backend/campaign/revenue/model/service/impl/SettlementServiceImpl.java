package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.RevenueSettlementCalculator;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueSettlementRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.MonthlySettlementRow;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SettlementService;
import java.time.LocalDate;
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

  @Override
  public void settleMonthly(int year, int month) {

    // 1) 해당 연/월의 정산 원천 데이터 로드
    List<MonthlySettlementRow> rows =
        revenueSettlementRepository.findMonthlySettlementRows(year, month);

    // 2) 정산 계산 + REVENUE 누적 반영
    revenueSettlementCalculator.settleMonth(year, month, rows);
  }

  @Override
  public void settleLastMonth() {
    LocalDate base = LocalDate.now().minusMonths(1);
    settleMonthly(base.getYear(), base.getMonthValue());
  }
}