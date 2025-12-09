package com.fiveguys.RIA.RIA_Backend.vip.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.VipStoreMonthlyProjection;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStoreSalesItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStoreSalesPageResponseDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VipStoreSalesPageLoader {

  private final SalesMonthlyRepository salesMonthlyRepository;

  public VipStoreSalesPageResponseDto load(Integer year, Integer month, int page, int size) {
    // 기준 월: year/month 없으면 SALES_MONTHLY 최신 달
    SalesMonthly latest = salesMonthlyRepository.findTopByOrderBySalesYearDescSalesMonthDesc();
    if (latest == null) {
      return VipStoreSalesPageResponseDto.builder()
          .year(0)
          .month(0)
          .page(page)
          .size(size)
          .totalElements(0L)
          .totalPages(0)
          .items(List.of())
          .build();
    }

    YearMonth target = (year != null && month != null)
        ? YearMonth.of(year, month)
        : YearMonth.of(latest.getSalesYear(), latest.getSalesMonth());

    int y = target.getYear();
    int m = target.getMonthValue();

    Pageable pageable = PageRequest.of(page, size, Sort.by("storeName").ascending());

    Page<VipStoreMonthlyProjection> currPage =
        salesMonthlyRepository.findVipStoreMonthlyPage(y, m, pageable);

    // 전월 데이터
    YearMonth prevYm = target.minusMonths(1);
    List<VipStoreMonthlyProjection> prevRows =
        salesMonthlyRepository.findVipStoreMonthlyAll(prevYm.getYear(), prevYm.getMonthValue());

    Map<Long, VipStoreMonthlyProjection> prevMap = prevRows.stream()
        .collect(Collectors.toMap(
            VipStoreMonthlyProjection::getStoreTenantMapId,
            Function.identity()
        ));

    List<VipStoreSalesItemResponseDto> items = currPage.getContent().stream()
        .map(row -> {
          BigDecimal vip = nvl(row.getVipSalesAmount());
          BigDecimal total = nvl(row.getTotalSalesAmount());
          BigDecimal ratio = calcRatio(vip, total);

          VipStoreMonthlyProjection prev = prevMap.get(row.getStoreTenantMapId());
          BigDecimal prevRatio = BigDecimal.ZERO;
          if (prev != null) {
            BigDecimal prevVip = nvl(prev.getVipSalesAmount());
            BigDecimal prevTotal = nvl(prev.getTotalSalesAmount());
            prevRatio = calcRatio(prevVip, prevTotal);
          }

          BigDecimal diff = ratio.subtract(prevRatio);

          return VipStoreSalesItemResponseDto.builder()
              .storeName(row.getStoreName())
              .vipRatio(ratio)
              .vipSalesAmount(vip)
              .totalSalesAmount(total)
              .momChange(diff)
              .build();
        })
        .collect(Collectors.toList());

    return VipStoreSalesPageResponseDto.builder()
        .year(y)
        .month(m)
        .page(currPage.getNumber())
        .size(currPage.getSize())
        .totalElements(currPage.getTotalElements())
        .totalPages(currPage.getTotalPages())
        .items(items)
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
        .divide(total, 1, RoundingMode.HALF_UP); // 1자리: 45.0%
  }
}
