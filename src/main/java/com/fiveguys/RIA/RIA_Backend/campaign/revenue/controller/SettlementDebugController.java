package com.fiveguys.RIA.RIA_Backend.campaign.revenue.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug/settlement")
@RequiredArgsConstructor
public class SettlementDebugController {

  private final SettlementService settlementService;

  @PostMapping("/monthly")
  public String settleMonthly(@RequestParam int year,
      @RequestParam int month) {
    settlementService.settleMonthly(year, month);
    return "OK";
  }

  @PostMapping("/last-month")
  public String settleLastMonth() {
    settlementService.settleLastMonth();
    return "OK";
  }
}