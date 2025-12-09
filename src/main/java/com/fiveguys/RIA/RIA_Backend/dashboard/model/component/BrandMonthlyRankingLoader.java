package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.BrandMonthlyRankingItem;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.BrandMonthlyRankingResponseDto;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.DashboardRepository;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.BrandMonthlyAmountProjection;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BrandMonthlyRankingLoader {

  private final DashboardRepository dashboardRepository;

  // 층 정보까지 포함
  private record BrandRow(
      String brandName,
      String floorName,
      BigDecimal currentAmount,
      double changeRate,
      String direction
  ) {}

  @Transactional(readOnly = true)
  public BrandMonthlyRankingResponseDto load(TargetMonthContext ctx) {
    if (ctx.isEmpty()) {
      return BrandMonthlyRankingResponseDto.builder()
          .year(0)
          .month(0)
          .hasPrevMonth(false)
          .hasNextMonth(false)
          .topBrands(List.of())
          .bottomBrands(List.of())
          .build();
    }

    int year = ctx.getYear();
    int month = ctx.getMonth();

    List<BrandMonthlyAmountProjection> currentList =
        dashboardRepository.findBrandMonthlyAmount(year, month);

    int prevYear = (month == 1) ? year - 1 : year;
    int prevMonth = (month == 1) ? 12 : month - 1;

    List<BrandMonthlyAmountProjection> prevList =
        dashboardRepository.findBrandMonthlyAmount(prevYear, prevMonth);

    Map<String, BigDecimal> prevMap = prevList.stream()
        .collect(Collectors.toMap(
            BrandMonthlyAmountProjection::getStoreName,
            p -> Optional.ofNullable(p.getTotalAmount()).orElse(BigDecimal.ZERO)
        ));

    List<BrandRow> ranked = currentList.stream()
        .map(row -> {
          BigDecimal current = Optional.ofNullable(row.getTotalAmount())
              .orElse(BigDecimal.ZERO);
          BigDecimal prev = prevMap.getOrDefault(row.getStoreName(), BigDecimal.ZERO);

          double changeRate;
          String direction;

          if (prev.compareTo(BigDecimal.ZERO) == 0) {
            changeRate = 0.0;
            direction = "up";
          } else {
            BigDecimal diff = current.subtract(prev);
            changeRate = diff
                .multiply(BigDecimal.valueOf(100))
                .divide(prev, 1, RoundingMode.HALF_UP)
                .doubleValue();
            direction = diff.signum() >= 0 ? "up" : "down";
          }

          return new BrandRow(
              row.getStoreName(),
              row.getFloorName(),  // ← 층 정보
              current,
              changeRate,
              direction
          );
        })
        .sorted(Comparator.comparing(BrandRow::currentAmount).reversed())
        .toList();

    int size = ranked.size();

    List<BrandMonthlyRankingItem> top = IntStream
        .range(0, Math.min(5, size))
        .mapToObj(i -> {
          BrandRow r = ranked.get(i);
          return BrandMonthlyRankingItem.builder()
              .rank(i + 1)
              .storeName(r.brandName())
              .floorName(r.floorName())          // ← 추가
              .totalAmount(r.currentAmount())
              .changeRate(BigDecimal.valueOf(r.changeRate()))
              .direction(r.direction())
              .build();
        })
        .toList();

    List<BrandMonthlyRankingItem> bottom = IntStream
        .range(0, Math.min(5, size))
        .mapToObj(i -> {
          BrandRow r = ranked.get(size - 1 - i);
          return BrandMonthlyRankingItem.builder()
              .rank(i + 1)
              .storeName(r.brandName())
              .floorName(r.floorName())          // ← 추가
              .totalAmount(r.currentAmount())
              .changeRate(BigDecimal.valueOf(r.changeRate()))
              .direction(r.direction())
              .build();
        })
        .toList();

    return BrandMonthlyRankingResponseDto.builder()
        .year(year)
        .month(month)
        .hasPrevMonth(ctx.isHasPrev())
        .hasNextMonth(ctx.isHasNext())
        .topBrands(top)
        .bottomBrands(bottom)
        .build();
  }
}
