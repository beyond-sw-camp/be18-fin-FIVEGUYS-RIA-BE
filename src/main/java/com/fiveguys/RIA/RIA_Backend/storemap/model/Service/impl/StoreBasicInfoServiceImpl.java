package com.fiveguys.RIA.RIA_Backend.storemap.model.Service.impl;

import com.fiveguys.RIA.RIA_Backend.storemap.model.Dto.StoreBasicInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Service.StoreBasicInfoService;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Service.StoreMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreBasicInfoServiceImpl implements StoreBasicInfoService {

    private final StoreMapService storeMapService;

    @Override
    public List<StoreBasicInfoResponseDto> getStoreNames() {
        return storeMapService.findAllActiveTenants().stream()
                .map(t -> new StoreBasicInfoResponseDto(
                        t.getStoreId(),
                        t.getStoreDisplayName()
                ))
                .toList();
    }
}
