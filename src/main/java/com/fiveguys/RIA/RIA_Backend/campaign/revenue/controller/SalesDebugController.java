package com.fiveguys.RIA.RIA_Backend.campaign.revenue.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SalesAggregationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug/sales")
@RequiredArgsConstructor
@Tag(name = "매출 집계 디버그", description = "일/월/연 단위 수동 집계 트리거 API (운영 비노출)")
public class SalesDebugController {

  private final SalesAggregationService salesAggregationService;

  @Operation(
      summary = "일별 매출 집계 실행",
      description = "지정한 연/월/일 기준으로 SALES_DAILY 집계를 강제 실행한다."
  )
  @PostMapping("/daily")
  public void aggregateDaily(
      @Parameter(description = "연도", example = "2024")
      @RequestParam int year,
      @Parameter(description = "월", example = "1")
      @RequestParam int month,
      @Parameter(description = "일", example = "15")
      @RequestParam int day
  ) {
    salesAggregationService.aggregateDaily(LocalDate.of(year, month, day));
  }

  @Operation(
      summary = "월별 매출 집계 실행",
      description = "지정한 연/월 기준으로 SALES_MONTHLY 집계를 강제 실행한다."
  )
  @PostMapping("/monthly")
  public void aggregateMonthly(
      @Parameter(description = "연도", example = "2024")
      @RequestParam int year,
      @Parameter(description = "월", example = "1")
      @RequestParam int month
  ) {
    salesAggregationService.aggregateMonth(year, month);
  }

  @Operation(
      summary = "연별 매출 집계 실행",
      description = "지정한 연도 기준으로 SALES_YEARLY 집계를 강제 실행한다."
  )
  @PostMapping("/yearly")
  public void aggregateYearly(
      @Parameter(description = "연도", example = "2024")
      @RequestParam int year
  ) {
    salesAggregationService.aggregateYear(year);
  }
}
