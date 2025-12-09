package com.fiveguys.RIA.RIA_Backend.storemap.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.storemap.model.dto.StoreDetailStatsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class StoreStatsMapper {
    public StoreDetailStatsResponseDto toResponse(
            Store store,
            StoreTenantMap tenant,
            StoreSalesStats stats,
            Contract contract
    ) {
        return StoreDetailStatsResponseDto.builder()

                .storeDisplayName(
                        tenant != null ? tenant.getStoreDisplayName() : null
                )
                .startDate(
                        tenant != null ? tenant.getStartDate() : null
                )

                .areaSize(
                        store.getAreaSize() != null
                                ? BigDecimal.valueOf(store.getAreaSize())
                                : null
                )

                .totalPurchaseCount(
                        stats != null ? stats.getTotalPurchaseCount() : 0
                )
                .totalSalesAmount(
                        stats != null ? stats.getTotalSalesAmount() : BigDecimal.ZERO
                )
                .vipPurchaseCount(
                        stats != null ? stats.getVipPurchaseCount() : 0
                )
                .vipSalesAmount(
                        stats != null ? stats.getVipSalesAmount() : BigDecimal.ZERO
                )
                .vipRatio(
                        stats != null ? stats.getVipRatio() : BigDecimal.ZERO
                )

                .contractStartDate(
                        contract != null ? contract.getContractStartDate() : null
                )
                .contractEndDate(
                        contract != null ? contract.getContractEndDate() : null
                )
                .finalContractAmount(
                        contract != null && contract.getTotalAmount() != null
                                ? BigDecimal.valueOf(contract.getTotalAmount())
                                : null
                )
                .commissionRate(
                        contract != null ? contract.getCommissionRate() : null
                )

                .build();
    }
}
