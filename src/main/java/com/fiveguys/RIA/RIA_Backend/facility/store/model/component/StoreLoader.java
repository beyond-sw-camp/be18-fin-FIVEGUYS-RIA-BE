package com.fiveguys.RIA.RIA_Backend.facility.store.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.StoreMapErrorCode;
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
        return storeRepository.findStoresByFloor(floorId);
    }

    public List<Store> loadAvailableStores(Long floorId,Store.StoreStatus status ,Store.StoreType type, String keyword){
        return storeRepository.searchAvailableStores(floorId,status ,type, keyword);
    }

    public Store loadStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(StoreMapErrorCode.INVALID_STORE_ID));
    }
}
