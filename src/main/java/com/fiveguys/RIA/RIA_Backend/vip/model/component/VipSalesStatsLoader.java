package com.fiveguys.RIA.RIA_Backend.vip.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.VipMonthlyAggProjection;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// VipSalesStatsLoader.java

@Component
@RequiredArgsConstructor
public class VipSalesStatsLoader {

  private final SalesMonthlyRepository salesMonthlyRepository;

  public VipStatsResponseDto load(Integer year, Integer month) {
    YearMonth target = resolveTargetYm(year, month);
    int y = target.getYear();
    int m = target.getMonthValue();

    // 이번 달 집계
    VipMonthlyAggProjection currAgg =
        salesMonthlyRepository.findVipMonthlyAgg(y, m);

    BigDecimal currTotal = nvl(currAgg.getTotalSalesAmount());
    BigDecimal currVip   = nvl(currAgg.getVipSalesAmount());
    BigDecimal currRatio = calcRatio(currVip, currTotal);

    // 전월 집계
    YearMonth prevYm = target.minusMonths(1);
    VipMonthlyAggProjection prevAgg =
        salesMonthlyRepository.findVipMonthlyAgg(prevYm.getYear(), prevYm.getMonthValue());

    BigDecimal prevTotal = nvl(prevAgg.getTotalSalesAmount());
    BigDecimal prevVip   = nvl(prevAgg.getVipSalesAmount());
    BigDecimal prevRatio = calcRatio(prevVip, prevTotal);

    BigDecimal diff = currRatio.subtract(prevRatio);
    String direction = decideDirection(diff);

    return VipStatsResponseDto.builder()
        .year(y)
        .month(m)
        .vipRatio(currRatio)
        .vipSalesAmount(currVip)
        .totalSalesAmount(currTotal)
        .prevVipRatio(prevRatio)
        .diffRatio(diff)
        .direction(direction)
        .build();
  }

  // year/month 둘 다 null이면 SALES_MONTHLY 최신 월을 사용
  private YearMonth resolveTargetYm(Integer year, Integer month) {
    if (year != null && month != null) {
      return YearMonth.of(year, month);
    }

    SalesMonthly latest = salesMonthlyRepository.findTopByOrderBySalesYearDescSalesMonthDesc();
    if (latest != null) {
      return YearMonth.of(latest.getSalesYear(), latest.getSalesMonth());
    }

    // SALES_MONTHLY 비어있을 때만 현재월 fallback
    return YearMonth.now();
  }

  private BigDecimal nvl(BigDecimal v) {
    return v == null ? BigDecimal.ZERO : v;
  }

  private BigDecimal calcRatio(BigDecimal vip, BigDecimal total) {
    if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
      return BigDecimal.ZERO;
    }
    return vip
        .multiply(BigDecimal.valueOf(100))
        .divide(total, 1, RoundingMode.HALF_UP);
  }

  private String decideDirection(BigDecimal diff) {
    int cmp = diff.compareTo(BigDecimal.ZERO);
    if (cmp > 0) return "up";
    if (cmp < 0) return "down";
    return "flat";
  }
}
