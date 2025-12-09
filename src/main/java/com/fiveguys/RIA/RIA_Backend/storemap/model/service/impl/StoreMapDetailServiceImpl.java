package com.fiveguys.RIA.RIA_Backend.storemap.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.StoreSalesStatsRepository;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreRepository;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreTenantMapRepository;
import com.fiveguys.RIA.RIA_Backend.storemap.model.component.StoreMapValidator;
import com.fiveguys.RIA.RIA_Backend.storemap.model.component.StoreStatsMapper;
import com.fiveguys.RIA.RIA_Backend.storemap.model.dto.StoreDetailStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.service.StoreMapDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreMapDetailServiceImpl implements StoreMapDetailService {
    private final StoreRepository storeRepository;
    private final StoreTenantMapRepository storeTenantMapRepository;
    private final StoreSalesStatsRepository storeSalesStatsRepository;

    private final StoreMapValidator validator;
    private final StoreStatsMapper mapper;

    @Override
    public StoreDetailStatsResponseDto getStoreDetail(Long storeId) {

        // 1. Store
        Store store = validator.requireStore(
                storeRepository.findById(storeId)
        );

        // 2. ACTIVE Tenant
        StoreTenantMap tenant = validator.optionalTenant(
                storeTenantMapRepository.findFirstByStore_StoreIdAndStatus(
                        storeId,
                        StoreTenantMap.Status.ACTIVE
                )
        );

        // 3. Sales Stats (tenant 기준)
        StoreSalesStats stats = validator.optionalStats(
                tenant != null
                        ? storeSalesStatsRepository.findByStoreTenantMapId(
                        tenant.getStoreTenantMapId()
                )
                        : Optional.empty()
        );

        // 4. Contract (tenant에서 바로)
        Contract contract = null;
        if (tenant != null) {
            contract = tenant.getContract();
        }

        // 5. Mapping
        return mapper.toResponse(store, tenant, stats, contract);
    }
}
