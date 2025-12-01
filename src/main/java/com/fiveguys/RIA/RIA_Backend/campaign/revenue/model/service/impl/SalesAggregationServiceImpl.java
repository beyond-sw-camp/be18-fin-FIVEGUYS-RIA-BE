package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.PosLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.PosValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.SalesDailyUpdater;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.StoreSalesStatsUpdater;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.VipCustomerLookupPort;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Pos;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SalesAggregationService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesAggregationServiceImpl implements SalesAggregationService {

  private final PosLoader posLoader;
  private final PosValidator posValidator;
  private final VipCustomerLookupPort vipCustomerLookupPort;
  private final SalesDailyUpdater salesDailyUpdater;
  private final StoreSalesStatsUpdater storeSalesStatsUpdater;

  /**
   * 역할: 집계 플로우의 오케스트레이션만 담당
   * - 데이터 로딩: PosLoader
   * - 검증: PosValidator
   * - VIP 판별: VipCustomerLookupPort
   * - 일 매출 업데이트: SalesDailyUpdater
   * - 누적 통계 업데이트: StoreSalesStatsUpdater
   *
   * 단일책임을 위해 실제 로직은 전부 컴포넌트로 위임한다.
   */
  @Override
  public void aggregateDaily(LocalDate targetDate) {

    List<Pos> posList = posLoader.loadFor(targetDate);

    for (Pos pos : posList) {

      if (!posValidator.isValid(pos)) {
        // 유효하지 않은 POS는 스킵 (로그 등은 내부 정책에 따라 추가)
        continue;
      }

      boolean isVip = vipCustomerLookupPort.isVipCustomer(pos.getCustomerId());

      salesDailyUpdater.update(pos, isVip);
      storeSalesStatsUpdater.update(pos, isVip);
    }
  }
}

