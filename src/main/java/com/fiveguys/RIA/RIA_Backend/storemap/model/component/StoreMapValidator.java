package com.fiveguys.RIA.RIA_Backend.storemap.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.StoreMapErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreTenantMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class StoreMapValidator {

    public Store requireStore(Optional<Store> storeOptional) {
        return storeOptional.orElseThrow(() ->
                new CustomException(StoreMapErrorCode.STORE_NOT_FOUND)
        );
    }

    public StoreTenantMap optionalTenant(Optional<StoreTenantMap> tenantOptional) {
        return tenantOptional.orElse(null); // 공실이면 null
    }

    public StoreSalesStats optionalStats(Optional<StoreSalesStats> statsOptional) {
        return statsOptional.orElse(null); // 없으면 null → 기본값
    }

    public StoreContractMap optionalContract(Optional<StoreContractMap> contractOptional) {
        return contractOptional.orElse(null); // 없으면 null
    }
}
