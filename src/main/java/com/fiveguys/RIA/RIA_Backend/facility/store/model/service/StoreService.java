package com.fiveguys.RIA.RIA_Backend.facility.store.model.service;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreListResponseDto;

public interface StoreService {
    StoreListResponseDto getStoresByFloor(Long floorId);
}
