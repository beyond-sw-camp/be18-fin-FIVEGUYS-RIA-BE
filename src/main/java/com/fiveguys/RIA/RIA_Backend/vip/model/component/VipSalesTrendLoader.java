// src/main/java/com/fiveguys/RIA/RIA_Backend/vip/model/component/VipSalesTrendLoader.java
package com.fiveguys.RIA.RIA_Backend.vip.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.VipSalesTrendProjection;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipSalesTrendItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipSalesTrendResponseDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VipSalesTrendLoader {

  private final SalesMonthlyRepository salesMonthlyRepository;

  public VipSalesTrendResponseDto loadLastSixMonths() {
    SalesMonthly latest = salesMonthlyRepository.findTopByOrderBySalesYearDescSalesMonthDesc();
    if (latest == null) {
      return VipSalesTrendResponseDto.builder()
          .baseYear(0)
          .baseMonth(0)
          .items(List.of())
          .sixMonthVipSales(BigDecimal.ZERO)
          .sixMonthTotalSales(BigDecimal.ZERO)
          .yoyChange(BigDecimal.ZERO)
          .build();
    }

    YearMonth end = YearMonth.of(latest.getSalesYear(), latest.getSalesMonth());
    YearMonth start = end.minusMonths(5);

    int fromYm = start.getYear() * 100 + start.getMonthValue();
    int toYm = end.getYear() * 100 + end.getMonthValue();

    // 최근 6개월 데이터
    List<VipSalesTrendProjection> currentRows =
        salesMonthlyRepository.findVipSalesTrendByYmRange(fromYm, toYm);

    // 월별 VIP 비중 리스트
    List<VipSalesTrendItemResponseDto> items = currentRows.stream()
        .map(row -> {
          BigDecimal vip = nvl(row.getVipSalesAmount());
          BigDecimal total = nvl(row.getTotalSalesAmount());
          BigDecimal ratio = calcRatio(vip, total);

          return VipSalesTrendItemResponseDto.builder()
              .year(row.getYear())
              .month(row.getMonth())
              .vipRatio(ratio)
              .build();
        })
        .collect(Collectors.toList());

    // 6개월 VIP / 전체 매출 합계
    BigDecimal sixMonthVip = currentRows.stream()
        .map(r -> nvl(r.getVipSalesAmount()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal sixMonthTotal = currentRows.stream()
        .map(r -> nvl(r.getTotalSalesAmount()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // 전년 동기간 (6개월) VIP 합계
    YearMonth prevEnd = end.minusYears(1);
    YearMonth prevStart = start.minusYears(1);

    int prevFromYm = prevStart.getYear() * 100 + prevStart.getMonthValue();
    int prevToYm = prevEnd.getYear() * 100 + prevEnd.getMonthValue();

    List<VipSalesTrendProjection> prevRows =
        salesMonthlyRepository.findVipSalesTrendByYmRange(prevFromYm, prevToYm);

    BigDecimal prevVipSum = prevRows.stream()
        .map(r -> nvl(r.getVipSalesAmount()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal yoy = calcYoyChange(sixMonthVip, prevVipSum);

    return VipSalesTrendResponseDto.builder()
        .baseYear(end.getYear())
        .baseMonth(end.getMonthValue())
        .items(items)
        .sixMonthVipSales(sixMonthVip)
        .sixMonthTotalSales(sixMonthTotal)
        .yoyChange(yoy)
        .build();
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

  private BigDecimal calcYoyChange(BigDecimal current, BigDecimal previous) {
    if (previous == null || previous.compareTo(BigDecimal.ZERO) <= 0) {
      return BigDecimal.ZERO;
    }
    return current
        .subtract(previous)
        .multiply(BigDecimal.valueOf(100))
        .divide(previous, 1, RoundingMode.HALF_UP);
  }
}
