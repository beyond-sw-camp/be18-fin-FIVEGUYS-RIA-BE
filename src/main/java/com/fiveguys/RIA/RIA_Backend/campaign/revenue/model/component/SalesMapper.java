package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.DailySalesHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.DailySalesItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SalesMapper {

  public DailySalesHistoryResponseDto toDailySalesHistoryResponseDto(
      Long storeTenantMapId,
      LocalDate startDate,
      LocalDate endDate,
      List<SalesDaily> dailyList
  ) {
    List<DailySalesItemResponseDto> items = dailyList.stream()
        .map(this::toDailySalesItemResponseDto)
        .collect(Collectors.toList());

    return DailySalesHistoryResponseDto.builder()
        .storeTenantMapId(storeTenantMapId)
        .startDate(startDate)
        .endDate(endDate)
        .items(items)
        .build();
  }

  public DailySalesItemResponseDto toDailySalesItemResponseDto(SalesDaily daily) {
    return DailySalesItemResponseDto.builder()
        .salesDate(daily.getSalesDate())
        .totalSalesAmount(daily.getTotalSalesAmount())
        .totalSalesCount(daily.getTotalSalesCount())
        .vipSalesAmount(daily.getVipSalesAmount())
        .vipSalesCount(daily.getVipSalesCount())
        .build();
  }
}