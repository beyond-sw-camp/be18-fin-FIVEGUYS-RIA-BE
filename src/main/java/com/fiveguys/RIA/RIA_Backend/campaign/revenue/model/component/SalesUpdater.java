package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesYearly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesDailyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesYearlyRepository;
import com.fiveguys.RIA.RIA_Backend.pos.model.entity.Pos;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SalesUpdater {

  private final SalesDailyRepository salesDailyRepository;
  private final SalesMonthlyRepository salesMonthlyRepository;
  private final SalesYearlyRepository salesYearlyRepository;
  private final PosValidator posValidator;
  private final VipCustomerLookupPort vipCustomerLookupPort;

  /**
   * POS 한 건 기준으로 해당 일자의 SALES_DAILY 갱신
   */
  @Transactional
  public void update(Pos pos, boolean isVip) {

    Long tenantMapId = pos.getStoreTenantMap().getStoreTenantMapId();
    LocalDate salesDate = pos.getPurchaseAt().toLocalDate();

    SalesDaily daily = salesDailyRepository
        .findByStoreTenantMapIdAndSalesDate(tenantMapId, salesDate)
        .orElseGet(() -> new SalesDaily(tenantMapId, salesDate));

    // 전체 매출 집계
    daily.increaseTotal(pos.getAmount());

    // VIP 매출 집계
    if (isVip) {
      daily.increaseVip(pos.getAmount());
    }

    salesDailyRepository.save(daily);
  }

  /**
   * 특정 일자 전체에 대해 POS 리스트 기반으로 SALES_DAILY 재빌드
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


  /**
   * DAILY 리스트 기반 월 집계 재빌드
   */
  @Transactional
  public void rebuildMonth(int year, int month, List<SalesDaily> dailyList) {

    Map<Long, SalesMonthly> monthlyMap = new HashMap<>();

    for (SalesDaily daily : dailyList) {
      Long tenantId = daily.getStoreTenantMapId();

      SalesMonthly monthly = monthlyMap.get(tenantId);
      if (monthly == null) {
        monthly = salesMonthlyRepository
            .findByStoreTenantMapIdAndSalesYearAndSalesMonth(tenantId, year, month)
            .orElseGet(() -> new SalesMonthly(tenantId, year, month));

        monthly.reset();
        monthlyMap.put(tenantId, monthly);
      }

      BigDecimal totalAmount = daily.getTotalSalesAmount();
      int totalCnt = daily.getTotalSalesCount();
      BigDecimal vipAmount = daily.getVipSalesAmount();
      int vipCnt = daily.getVipSalesCount();

      monthly.addDaily(totalAmount, totalCnt, vipAmount, vipCnt);
    }

    salesMonthlyRepository.saveAll(monthlyMap.values());
  }

  /**
   * MONTHLY 리스트 기반 연 집계 재빌드
   */
  @Transactional
  public void rebuildYear(int year, List<SalesMonthly> monthlyList) {

    Map<Long, SalesYearly> yearlyMap = new HashMap<>();

    for (SalesMonthly monthly : monthlyList) {
      Long tenantId = monthly.getStoreTenantMapId();

      SalesYearly yearly = yearlyMap.get(tenantId);
      if (yearly == null) {
        yearly = salesYearlyRepository
            .findByStoreTenantMapIdAndSalesYear(tenantId, year)
            .orElseGet(() -> new SalesYearly(tenantId, year));

        yearly.reset();
        yearlyMap.put(tenantId, yearly);
      }

      yearly.addPeriod(
          monthly.getTotalSalesAmount(),
          monthly.getTotalSalesCount(),
          monthly.getVipSalesAmount(),
          monthly.getVipSalesCount()
      );
    }

    salesYearlyRepository.saveAll(yearlyMap.values());
  }
}
