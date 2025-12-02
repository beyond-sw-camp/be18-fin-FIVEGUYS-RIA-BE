package com.fiveguys.RIA.RIA_Backend.storemap.model.Service;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreTenantMap;

import java.util.List;
import java.util.Optional;

public interface StoreMapService {
    Optional<Store> findStore(Long storeId);

    Optional<StoreTenantMap> findActiveTenant(Long storeId);

    Optional<StoreSalesStats> findStats(Long tenantMapId);

    Optional<StoreContractMap> findActiveContract(Long storeId);

    List<Store> findAllStores();

    List<StoreTenantMap> findAllActiveTenants();

}
