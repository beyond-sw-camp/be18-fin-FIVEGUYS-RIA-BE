package com.fiveguys.RIA.RIA_Backend.facility.store.model.service;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;

public interface StoreService {
    StoreListResponseDto getStoresByFloor(Long floorId);

    StoreListResponseDto getAvailableSpaces(Long floorId, String keyword,Store.StoreType type);

    StoreDetailResponseDto getStoreDetail(Long storeId);
}
