package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.scheduler;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SalesAggregationService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalesScheduler {

  private final SalesAggregationService salesAggregationService;

  // 매일 00:05에 "어제" POS → DAILY/STATS 집계
  @Scheduled(cron = "0 5 0 * * *")
  public void aggregateYesterday() {
    LocalDate targetDate = LocalDate.now().minusDays(1);
    salesAggregationService.aggregateDaily(targetDate);
  }

  // 매달 1일 00:10에 "지난달" DAILY → MONTHLY 집계
  @Scheduled(cron = "0 10 0 1 * *")
  public void aggregateLastMonth() {
    LocalDate now = LocalDate.now();
    LocalDate lastMonth = now.withDayOfMonth(1).minusMonths(1);

    int year = lastMonth.getYear();
    int month = lastMonth.getMonthValue();

    salesAggregationService.aggregateMonth(year, month);
  }

  @Scheduled(cron = "0 20 0 1 1 *")
  public void aggregateForLastYear() {
    int lastYear = LocalDate.now().minusYears(1).getYear();
    salesAggregationService.aggregateYear(lastYear);
  }
}