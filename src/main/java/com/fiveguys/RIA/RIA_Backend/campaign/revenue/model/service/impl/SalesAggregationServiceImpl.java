package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.PosLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.PosValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.SalesLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.SalesUpdater;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.StoreSalesStatsUpdater;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.VipCustomerLookupPort;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Pos;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesYearly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesYearlyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.SalesAggregationService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesAggregationServiceImpl implements SalesAggregationService {

  private final PosLoader posLoader;
  private final PosValidator posValidator;
  private final VipCustomerLookupPort vipCustomerLookupPort;
  private final SalesUpdater salesUpdater;
  private final StoreSalesStatsUpdater storeSalesStatsUpdater;
  private final SalesLoader salesLoader;

  // 역할: 집계 플로우의 오케스트레이션만 담당
  // 데이터 로딩: PosLoader
  // 검증: PosValidator
  // VIP 판별: VipCustomerLookupPort
  // 일 매출 업데이트: SalesUpdater
  // 누적 통계 업데이트: StoreSalesStatsUpdater
  // 단일책임을 위해 실제 로직은 전부 컴포넌트로
  @Override
  public void aggregateDaily(LocalDate targetDate) {

    List<Pos> posList = posLoader.loadFor(targetDate);

    for (Pos pos : posList) {

      if (!posValidator.isValid(pos)) {
        // 유효하지 않은 POS는 스킵 (로그 등은 내부 정책에 따라 추가)
        continue;
      }

      boolean isVip = vipCustomerLookupPort.isVipCustomer(pos.getCustomerId());

      salesUpdater.update(pos, isVip);
      storeSalesStatsUpdater.update(pos, isVip);
    }
  }
  // 역할: "언제, 어떤 월을 집계할지"만 결정하는 오케스트레이터
  // 데이터 로딩: SalesDailyLoader
  // 월 집계 계산/저장: SalesUpdater
  @Override
  public void aggregateMonth(int year, int month) {

    List<SalesDaily> dailyList = salesLoader.loadForMonth(year, month);

    salesUpdater.rebuildMonth(year, month, dailyList);
  }
  // 역할: "언제, 어떤 년을 집계할지"만 결정하는 오케스트레이터
  @Override
  public void aggregateYear(int year) {

    // 1. 해당 연도의 월매출 전체 로딩 (로더만 사용)
    List<SalesMonthly> monthlyList = salesLoader.loadForYear(year);

    // 2. 실제 집계/저장은 SalesUpdater에 위임
    salesUpdater.rebuildYear(year, monthlyList);
  }
}

