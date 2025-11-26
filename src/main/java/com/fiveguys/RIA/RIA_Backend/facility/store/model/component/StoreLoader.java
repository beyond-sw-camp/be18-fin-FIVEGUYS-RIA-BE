package com.fiveguys.RIA.RIA_Backend.facility.store.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.StoreErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreLoader {

    private final StoreRepository storeRepository;

    public List<Store> loadStores(Long floorId) {
        List<Store> stores = storeRepository.findStoresByFloor(floorId);

        if (stores.isEmpty()) {
            throw new CustomException(StoreErrorCode.STORE_NOT_FOUND);
        }

        return stores;
    }
}
