package com.fiveguys.RIA.RIA_Backend.storemap.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDetailStatsResponseDto {

    private String storeDisplayName;
    private LocalDate startDate;
    private BigDecimal areaSize;

    private Integer totalPurchaseCount;
    private BigDecimal totalSalesAmount;
    private Integer vipPurchaseCount;
    private BigDecimal vipSalesAmount;
    private BigDecimal vipRatio;

    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private BigDecimal finalContractAmount;
    private BigDecimal commissionRate;
}
