package com.fiveguys.RIA.RIA_Backend.vip.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.VipBrandRatioProjection;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipBrandTop5ResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipBrandTopItemResponseDto;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VipBrandTop5Loader {

  private final SalesMonthlyRepository salesMonthlyRepository;

  public VipBrandTop5ResponseDto load(Integer year, Integer month) {
    YearMonth target = resolveTargetYm(year, month);
    int y = target.getYear();
    int m = target.getMonthValue();

    List<VipBrandRatioProjection> rows =
        salesMonthlyRepository.findTop5VipBrandRatio(y, m);

    AtomicInteger rank = new AtomicInteger(1);
    List<VipBrandTopItemResponseDto> items = rows.stream()
        .map(r -> VipBrandTopItemResponseDto.builder()
            .rank(rank.getAndIncrement())
            .name(r.getStoreName())
            .rate(r.getVipRatio())
            .build())
        .collect(Collectors.toList());

    return VipBrandTop5ResponseDto.builder()
        .year(y)
        .month(m)
        .items(items)
        .build();
  }

  private YearMonth resolveTargetYm(Integer year, Integer month) {
    if (year != null && month != null) {
      return YearMonth.of(year, month);
    }

    SalesMonthly latest = salesMonthlyRepository.findTopByOrderBySalesYearDescSalesMonthDesc();
    if (latest != null) {
      return YearMonth.of(latest.getSalesYear(), latest.getSalesMonth());
    }

    return YearMonth.now();
  }
}
