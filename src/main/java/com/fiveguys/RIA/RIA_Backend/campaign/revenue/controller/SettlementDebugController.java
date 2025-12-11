package com.fiveguys.RIA.RIA_Backend.campaign.revenue.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug/settlement")
@RequiredArgsConstructor
@Tag(name = "정산 집계 디버그", description = "상설/임시 정산 수동 실행 API (운영 비노출)")
public class SettlementDebugController {

  private final SettlementService settlementService;

  @Operation(
      summary = "상설 매장의 월 정산 실행",
      description = "특정 연·월에 대한 REGULAR 매장의 월 정산을 강제 실행한다."
  )
  @PostMapping("/monthly")
  public String settleMonthly(
      @Parameter(description = "연도", example = "2024")
      @RequestParam int year,
      @Parameter(description = "월", example = "1")
      @RequestParam int month
  ) {
    settlementService.settleMonthlyForRegular(year, month);
    return "OK";
  }

  @Operation(
      summary = "상설 매장의 지난달 월 정산 실행",
      description = "현재 시점 기준 지난달의 REGULAR 매장 월 정산을 강제 실행한다."
  )
  @PostMapping("/last-month")
  public String settleLastMonth() {
    settlementService.settleLastMonthForRegular();
    return "OK";
  }

  @Operation(
      summary = "임시 매장(팝업/전시)의 일 정산 실행",
      description = "지정한 날짜(LocalDate)에 대해 TEMPORARY 매장의 일 정산을 강제 실행한다."
  )
  @PostMapping("/daily/temporary")
  public String settleDailyTemporary(
      @Parameter(description = "정산 대상 일자 (YYYY-MM-DD)", example = "2023-01-15")
      @RequestParam String date
  ) {
    LocalDate target = LocalDate.parse(date);
    settlementService.settleDailyForTemporary(target);
    return "OK";
  }

  @Operation(
      summary = "임시 매장(팝업/전시)의 어제 일 정산 실행",
      description = "현재 기준 어제 날짜로 TEMPORARY 매장의 일 정산을 강제 실행한다."
  )
  @PostMapping("/daily/temporary/yesterday")
  public String settleDailyTemporaryYesterday() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    settlementService.settleDailyForTemporary(yesterday);
    return "OK";
  }
}
