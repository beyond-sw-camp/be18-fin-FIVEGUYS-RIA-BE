package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesDailyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalesLoader {

  private final SalesDailyRepository salesDailyRepository;
  private final SalesMonthlyRepository salesMonthlyRepository;

  // 역할: 특정 월(year, month)에 해당하는 일별 매출 전체 로딩
  // - 데이터 접근(infra) 책임만 가진다.
  public List<SalesDaily> loadForMonth(int year, int month) {
    LocalDate start = LocalDate.of(year, month, 1);
    LocalDate end = start.plusMonths(1).minusDays(1);
    return salesDailyRepository.findBySalesDateBetween(start, end);
  }

  // 특정 연도의 월별 매출 전체 로딩
  public List<SalesMonthly> loadForYear(int year) {
    return salesMonthlyRepository.findBySalesYear(year);
  }
}