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

  // 1) 매월 1일 01:00 - 지난달 상설(정규) 매장 월 정산
  @Scheduled(cron = "0 0 1 1 * ?")
  public void runMonthlySettlementJobForRegular() {
    log.info("[SETTLEMENT] Monthly settlement job for REGULAR started");
    settlementService.settleLastMonthForRegular();
    log.info("[SETTLEMENT] Monthly settlement job for REGULAR finished");
  }

  // 2) 매일 02:00 - 전날 팝업/전시 매장 일 정산
  @Scheduled(cron = "0 0 2 * * ?")
  public void runDailySettlementJobForPopup() {
    log.info("[SETTLEMENT] Daily settlement job for POPUP/EXHIBITION started");
    settlementService.settleYesterdayForTemporary();
    log.info("[SETTLEMENT] Daily settlement job for POPUP/EXHIBITION finished");
  }
}
