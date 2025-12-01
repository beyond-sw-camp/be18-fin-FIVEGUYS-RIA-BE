package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Pos;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesDailyRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalesDailyUpdater {

  private final SalesDailyRepository salesDailyRepository;

  /**
   * 역할: POS 한 건을 받아서 해당 일자의 SALES_DAILY만 갱신
   * - 일 매출 집계 책임만 가진다.
   * - 다른 통계/정산 로직과 분리하여 단일책임 유지.
   */
  public void update(Pos pos, boolean isVip) {
    Long tenantId = pos.getStoreTenantMapId();
    LocalDate salesDate = pos.getPurchaseAt().toLocalDate();

    SalesDaily daily = salesDailyRepository
        .findByStoreTenantMapIdAndSalesDate(tenantId, salesDate)
        .orElseGet(() -> new SalesDaily(tenantId, salesDate));

    daily.increaseTotal(pos.getAmount());
    if (isVip) {
      daily.increaseVip(pos.getAmount());
    }

    salesDailyRepository.save(daily);
  }
}
