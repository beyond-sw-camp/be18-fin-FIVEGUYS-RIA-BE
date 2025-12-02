package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.StoreSalesStatsRepository;
import com.fiveguys.RIA.RIA_Backend.pos.model.entity.Pos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreSalesStatsUpdater {

  private final StoreSalesStatsRepository storeSalesStatsRepository;

  /**
   * 역할: 매장(테넌트) 단위 누적 통계(STORE_SALES_STATS)만 갱신
   * - VIP/일반 거래 수, 금액, VIP 비율 계산 책임을 이 컴포넌트에 집중
   * - 다른 집계/정산 로직과 분리해 테스트/변경 용이하게 유지
   */
  public void update(Pos pos, boolean isVip) {
    Long tenantId = pos.getStoreTenantMapId();

    StoreSalesStats stats = storeSalesStatsRepository
        .findByStoreTenantMapId(tenantId)
        .orElseGet(() -> new StoreSalesStats(tenantId));

    if (isVip) {
      stats.increaseVip(pos.getAmount(), pos.getPurchaseAt());
    } else {
      stats.increaseNonVip(pos.getAmount(), pos.getPurchaseAt());
    }

    storeSalesStatsRepository.save(stats);
  }
}
