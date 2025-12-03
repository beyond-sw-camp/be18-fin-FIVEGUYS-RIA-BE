package com.fiveguys.RIA.RIA_Backend.campaign.revenue.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SalesAggregationService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug/sales")
@RequiredArgsConstructor
public class SalesDebugController {

  private final SalesAggregationService salesAggregationService;

  // 1) 일별 집계
  @PostMapping("/daily")
  public void aggregateDaily(@RequestParam int year,
      @RequestParam int month,
      @RequestParam int day) {
    salesAggregationService.aggregateDaily(LocalDate.of(year, month, day));
  }

  // 2) 월별 집계
  @PostMapping("/monthly")
  public void aggregateMonthly(@RequestParam int year,
      @RequestParam int month) {
    salesAggregationService.aggregateMonth(year, month);
  }

  // 3) 연별 집계
  @PostMapping("/yearly")
  public void aggregateYearly(@RequestParam int year) {
    salesAggregationService.aggregateYear(year);
  }
}