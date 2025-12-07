package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.scheduler;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementScheduler {

  private final SettlementService settlementService;

  //매월 1일 01:00 에 지난달 정산 실행
  @Scheduled(cron = "0 0 1 1 * ?")
  public void runMonthlySettlementJob() {
    log.info("[SETTLEMENT] Monthly settlement job started");
    settlementService.settleLastMonth();
    log.info("[SETTLEMENT] Monthly settlement job finished");
  }
}