package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Pos;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesYearly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesDailyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesYearlyRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalesUpdater {

  private final SalesDailyRepository salesDailyRepository;
  private final SalesMonthlyRepository salesMonthlyRepository;
  private final SalesYearlyRepository salesYearlyRepository;

  // 역할: POS 한 건을 받아서 해당 일자의 SALES_DAILY만 갱신
  // 일 매출 집계 책임만 가진다.
  // 다른 통계/정산 로직과 분리하여 단일책임 유지.
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

//   역할: 주어진 연/월 + DAILY 리스트를 받아서
//   SALES_MONTHLY 테이블을 재계산/반영한다.
//   월 집계 로직의 단일 책임 컴포넌트.
//   오케스트레이터(ServiceImpl)는 "언제/어떤 월을 집계할지"만 결정하고,
//   실제 계산/저장은 전부 여기로 위임한다.
  public void rebuildMonth(int year, int month, List<SalesDaily> dailyList) {

    // tenantId -> SalesMonthly (메모리 상 계산용)
    Map<Long, SalesMonthly> monthlyMap = new HashMap<>();

    for (SalesDaily daily : dailyList) {
      Long tenantId = daily.getStoreTenantMapId();

      SalesMonthly monthly = monthlyMap.get(tenantId);
      if (monthly == null) {
        // 기존 MONTHLY row 있으면 불러오고, 없으면 새로 만든다.
        monthly = salesMonthlyRepository
            .findByStoreTenantMapIdAndSalesYearAndSalesMonth(tenantId, year, month)
            .orElseGet(() -> new SalesMonthly(tenantId, year, month));

        // 월 전체를 다시 계산하기 때문에 기존 값은 초기화한다.
        monthly.reset();
        monthlyMap.put(tenantId, monthly);
      }

      BigDecimal totalAmount = daily.getTotalSalesAmount();
      int totalCnt = daily.getTotalSalesCount();
      BigDecimal vipAmount = daily.getVipSalesAmount();
      int vipCnt = daily.getVipSalesCount();

      monthly.addDaily(totalAmount, totalCnt, vipAmount, vipCnt);
    }

    // 최종 결과 한번에 저장
    salesMonthlyRepository.saveAll(monthlyMap.values());
  }

  public void rebuildYear(int year, List<SalesMonthly> monthlyList) {

    Map<Long, SalesYearly> yearlyMap = new HashMap<>();

    for (SalesMonthly monthly : monthlyList) {
      Long tenantId = monthly.getStoreTenantMapId();

      SalesYearly yearly = yearlyMap.get(tenantId);
      if (yearly == null) {
        yearly = salesYearlyRepository
            .findByStoreTenantMapIdAndSalesYear(tenantId, year)
            .orElseGet(() -> new SalesYearly(tenantId, year));

        yearly.reset(); // 연 전체 재계산
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
