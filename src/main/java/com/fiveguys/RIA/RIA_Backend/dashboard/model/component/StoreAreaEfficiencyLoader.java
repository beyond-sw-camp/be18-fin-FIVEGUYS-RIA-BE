// src/main/java/com/fiveguys/RIA/RIA_Backend/dashboard/model/component/StoreAreaEfficiencyLoader.java
package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreAreaEfficiencyItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreAreaEfficiencyRankingResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.DashboardRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.StoreAreaEfficiencyProjection;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreAreaEfficiencyLoader {

  private final DashboardRepository dashboardRepository;

  public StoreAreaEfficiencyRankingResponseDto load(TargetMonthContext ctx) {

    // 컨텍스트 방어
    if (ctx == null
        || ctx.isEmpty()
        || ctx.getYear() <= 0
        || ctx.getMonth() <= 0) {

      return StoreAreaEfficiencyRankingResponseDto.builder()
          .year(0)
          .month(0)
          .hasPrevMonth(false)
          .hasNextMonth(false)
          .topStores(List.of())
          .bottomStores(List.of())
          .build();
    }

    List<StoreAreaEfficiencyProjection> rows =
        dashboardRepository.findStoreAreaEfficiency(ctx.getYear(), ctx.getMonth());

    // 프로젝션 → DTO 매핑 + 효율 계산
    List<StoreAreaEfficiencyItem> items = rows.stream()
        .map(r -> {
          BigDecimal areaSize =
              r.getAreaSize() == null ? null : BigDecimal.valueOf(r.getAreaSize());
          BigDecimal finalRevenue =
              r.getFinalRevenue() == null ? BigDecimal.ZERO : r.getFinalRevenue();

          BigDecimal efficiency = BigDecimal.ZERO;
          if (areaSize != null && areaSize.compareTo(BigDecimal.ZERO) > 0) {
            efficiency = finalRevenue.divide(areaSize, 0, RoundingMode.HALF_UP);
          }

          return StoreAreaEfficiencyItem.builder()
              .rank(0) // 이후에 세팅
              .storeTenantMapId(r.getStoreTenantMapId())
              .storeName(r.getStoreName())
              .floorName(r.getFloorName())
              .areaSize(areaSize)
              .finalRevenue(finalRevenue)
              .efficiency(efficiency)
              .build();
        })
        .collect(Collectors.toList());

    // 효율 기준 정렬
    List<StoreAreaEfficiencyItem> sortedDesc = items.stream()
        .sorted(Comparator.comparing(StoreAreaEfficiencyItem::getEfficiency).reversed())
        .collect(Collectors.toList());

    // 상위 TOP5
    List<StoreAreaEfficiencyItem> top = sortedDesc.stream()
        .limit(5)
        .collect(Collectors.toList());
    int rank = 1;
    for (StoreAreaEfficiencyItem item : top) {
      item.setRank(rank++);
    }

    // 하위 TOP5 (효율 오름차순)
    List<StoreAreaEfficiencyItem> sortedAsc = items.stream()
        .sorted(Comparator.comparing(StoreAreaEfficiencyItem::getEfficiency))
        .collect(Collectors.toList());
    List<StoreAreaEfficiencyItem> bottom = sortedAsc.stream()
        .limit(5)
        .collect(Collectors.toList());
    rank = 1;
    for (StoreAreaEfficiencyItem item : bottom) {
      item.setRank(rank++);
    }

    return StoreAreaEfficiencyRankingResponseDto.builder()
        .year(ctx.getYear())
        .month(ctx.getMonth())
        .hasPrevMonth(ctx.isHasPrev())
        .hasNextMonth(ctx.isHasNext())
        .topStores(top)
        .bottomStores(bottom)
        .build();
  }
}
