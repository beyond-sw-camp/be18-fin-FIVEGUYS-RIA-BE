package com.fiveguys.RIA.RIA_Backend.storemap.model.Service;

import com.fiveguys.RIA.RIA_Backend.storemap.model.Dto.StoreDetailStatsResponseDto;

public interface StoreDetailStatsService {

    StoreDetailStatsResponseDto getStoreDetail(Long storeId);
}
