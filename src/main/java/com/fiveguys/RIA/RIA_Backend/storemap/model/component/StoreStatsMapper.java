package com.fiveguys.RIA.RIA_Backend.storemap.model.component;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Dto.StoreDetailStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreTenantMap;
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
            StoreContractMap contract
    ) {
        return StoreDetailStatsResponseDto.builder()

                // ======================================
                // STORE_TENANT_MAP 영역
                // ======================================
                .storeDisplayName(tenant != null ? tenant.getStoreDisplayName() : null)
                .startDate(tenant != null ? tenant.getStartDate() : null)

                // ======================================
                // STORE 영역 (facility.Store)
                // areaSize는 Double → BigDecimal 변환 필요
                // ======================================
                .areaSize(store.getAreaSize() != null
                        ? BigDecimal.valueOf(store.getAreaSize())
                        : null)

                // ======================================
                // STORE_SALES_STATS (null이면 기본값)
                // ======================================
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

                // ======================================
                // STORE_CONTRACT_MAP
                // ======================================
                .contractStartDate(
                        contract != null ? contract.getContractStartDate() : null
                )
                .contractEndDate(
                        contract != null ? contract.getContractEndDate() : null
                )
                .finalContractAmount(
                        contract != null ? contract.getFinalContractAmount() : null
                )
                .commissionRate(
                        contract != null ? contract.getCommissionRate() : null
                )

                .build();
    }
}
