package com.fiveguys.RIA.RIA_Backend.storemap.model.Service;

import com.fiveguys.RIA.RIA_Backend.storemap.model.Dto.StoreBasicInfoResponseDto;

import java.util.List;

public interface StoreBasicInfoService {

    List<StoreBasicInfoResponseDto> getStoreNames();
}
