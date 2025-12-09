package com.fiveguys.RIA.RIA_Backend.storemap.model.service;

import com.fiveguys.RIA.RIA_Backend.storemap.model.dto.StoreDetailStatsResponseDto;

public interface StoreMapDetailService {
    StoreDetailStatsResponseDto getStoreDetail(Long storeId);
}
