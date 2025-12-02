package com.fiveguys.RIA.RIA_Backend.storemap.model.Service.impl;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Service.StoreMapService;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.storemap.model.repository.StoreContractMapRepository;
import com.fiveguys.RIA.RIA_Backend.storemap.model.repository.StoreRepository;
import com.fiveguys.RIA.RIA_Backend.storemap.model.repository.StoreSalesStatsRepository;
import com.fiveguys.RIA.RIA_Backend.storemap.model.repository.StoreTenantMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreMapServiceImpl implements StoreMapService {

    private final StoreRepository storeRepository;
    private final StoreTenantMapRepository tenantMapRepository;
    private final StoreSalesStatsRepository statsRepository;
    private final StoreContractMapRepository contractMapRepository;

    @Override
    public Optional<Store> findStore(Long storeId) {
        return storeRepository.findById(storeId);
    }

    @Override
    public Optional<StoreTenantMap> findActiveTenant(Long storeId) {
        return tenantMapRepository.findByStoreIdAndStatus(storeId, StoreTenantMap.Status.ACTIVE);
    }

    @Override
    public Optional<StoreSalesStats> findStats(Long tenantMapId) {
        return statsRepository.findByStoreTenantMapId(tenantMapId);
    }

    @Override
    public Optional<StoreContractMap> findActiveContract(Long storeId) {
        return contractMapRepository.findByStoreId(storeId)
                .stream()
                .findFirst();

    }

    @Override
    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public List<StoreTenantMap> findAllActiveTenants() {
        return tenantMapRepository.findByStatus(StoreTenantMap.Status.ACTIVE);
    }
}
