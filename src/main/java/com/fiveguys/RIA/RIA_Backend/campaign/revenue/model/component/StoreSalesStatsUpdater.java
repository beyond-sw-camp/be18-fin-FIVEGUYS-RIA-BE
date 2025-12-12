package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.StoreSalesStatsRepository;
import com.fiveguys.RIA.RIA_Backend.pos.model.entity.Pos;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StoreSalesStatsUpdater {

  private final StoreSalesStatsRepository storeSalesStatsRepository;
  private final PosValidator posValidator;
  private final VipCustomerLookupPort vipCustomerLookupPort;

  /**
   * 매장(테넌트) 단위 누적 통계(STORE_SALES_STATS) 한 건 갱신
   */
  @Transactional
  public void update(Pos pos, boolean isVip) {
    Long tenantId = pos.getStoreTenantMapId(); // Pos 엔티티에 정의된 헬퍼 사용

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

  /**
   * 특정 일자(targetDate)에 대해 STORE_SALES_STATS 를 전부 재빌드 - targetDate 기준 기존 통계 삭제 - POS 리스트 기반으로 다시 누적
   */
  @Transactional
  public void rebuildDaily(LocalDate targetDate, List<Pos> posList) {

    for (Pos pos : posList) {

      if (!pos.getPurchaseAt().toLocalDate().equals(targetDate)) {
        continue;
      }

      if (!posValidator.isValid(pos)) {
        continue;
      }

      boolean isVip = vipCustomerLookupPort.isVipCustomer(pos.getCustomerId());

      update(pos, isVip);
    }
  }
}
