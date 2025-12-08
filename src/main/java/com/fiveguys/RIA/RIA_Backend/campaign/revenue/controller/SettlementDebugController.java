package com.fiveguys.RIA.RIA_Backend.campaign.revenue.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SettlementService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug/settlement")
@RequiredArgsConstructor
public class SettlementDebugController {

  private final SettlementService settlementService;

  // 상설(정규) 월 정산
  @PostMapping("/monthly")
  public String settleMonthly(
      @RequestParam int year,
      @RequestParam int month
  ) {
    settlementService.settleMonthlyForRegular(year, month);
    return "OK";
  }

  // 상설(정규) 지난달 월 정산
  @PostMapping("/last-month")
  public String settleLastMonth() {
    settlementService.settleLastMonthForRegular();
    return "OK";
  }

  // 임시(팝업/전시) 일 정산
  // 예) POST /debug/settlement/daily/temporary?date=2023-01-15
  @PostMapping("/daily/temporary")
  public String settleDailyTemporary(@RequestParam String date) {
    LocalDate target = LocalDate.parse(date);
    settlementService.settleDailyForTemporary(target);
    return "OK";
  }

  // 임시(팝업/전시) 어제 일 정산
  // 예) POST /debug/settlement/daily/temporary/yesterday
  @PostMapping("/daily/temporary/yesterday")
  public String settleDailyTemporaryYesterday() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    settlementService.settleDailyForTemporary(yesterday);
    return "OK";
  }

}
