package com.fiveguys.RIA.RIA_Backend.facility.store.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.component.FloorLoader;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.component.StoreLoader;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.component.StoreMapper;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreLoader loader;
    private final StoreMapper mapper;
    private final FloorLoader floorLoader;

    @Override
    public StoreListResponseDto getStoresByFloor(Long floorId) {

        // 1. Floor 로딩
        Floor floor = floorLoader.loadFloor(floorId);

        // 2. 매장 목록 조회
        var stores = loader.loadStores(floorId);

        // 3. 응답 DTO 생성
        return mapper.toListDto(
                floor.getFloorId(),
                floor.getFloorName().name(),
                stores
        );
    }
}
