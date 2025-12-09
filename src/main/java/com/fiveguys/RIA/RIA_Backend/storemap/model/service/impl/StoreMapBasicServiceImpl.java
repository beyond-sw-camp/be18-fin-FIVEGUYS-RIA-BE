package com.fiveguys.RIA.RIA_Backend.storemap.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.StoreMapErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreRepository;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreTenantMapRepository;
import com.fiveguys.RIA.RIA_Backend.storemap.model.dto.StoreBasicInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.service.StoreMapBasicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreMapBasicServiceImpl implements StoreMapBasicService {

    private final StoreRepository storeRepository;
    private final StoreTenantMapRepository storeTenantMapRepository;

    @Override
    public List<StoreBasicInfoResponseDto> getAllBasicInfo() {

        List<Store> stores = storeRepository.findAll();

        return stores.stream()
                .map(store -> {

                    StoreTenantMap tenant =
                            storeTenantMapRepository
                                    .findFirstByStore_StoreIdAndStatus(
                                            store.getStoreId(),
                                            StoreTenantMap.Status.ACTIVE
                                    )
                                    .orElse(null);

                    String displayName =
                            tenant != null
                                    ? tenant.getStoreDisplayName()
                                    : "공실";

                    return new StoreBasicInfoResponseDto(
                            store.getStoreId(),
                            displayName
                    );
                })
                .toList();
    }
}


