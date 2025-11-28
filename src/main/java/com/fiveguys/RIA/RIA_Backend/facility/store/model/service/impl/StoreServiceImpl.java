package com.fiveguys.RIA.RIA_Backend.facility.store.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.component.FloorLoader;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.component.StoreLoader;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.component.StoreMapper;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.component.StoreValidator;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreLoader storeLoader;
    private final StoreMapper storeMapper;
    private final StoreValidator storeValidator;
    private final FloorLoader floorLoader;

    @Override
    public StoreListResponseDto getStoresByFloor(Long floorId) {

        // 1. Floor 로딩
        Floor floor = floorLoader.loadFloor(floorId);

        // 2. 매장 목록 조회
        List<Store> stores = storeLoader.loadStores(floorId);

        // 3. 응답 DTO 생성
        return storeMapper.toListDto(
                floor.getFloorId(),
                floor.getFloorName().name(),
                stores
        );
    }

    @Override
    public StoreListResponseDto getAvailableSpaces(Long floorId,String keyword,Store.StoreType type) {

        // 1. 검증
        storeValidator.validateFloorId(floorId);
        storeValidator.validateKeyword(keyword);

        // 2. Floor 조회
        Floor floor = floorLoader.loadFloor(floorId);

        // 3. AVAILABLE 공간 조회
        List<Store> spaces = storeLoader.loadAvailableStores(floorId,Store.StoreStatus.AVAILABLE,type, keyword);

        // 4. 비즈니스 검증 (AVAILABLE 없음)
        storeValidator.validateSpacesExist(spaces);

        // 5. DTO 반환
        return storeMapper.toListDto(
                floor.getFloorId(),
                floor.getFloorName().name(),
                spaces
        );
    }

    @Override
    public StoreDetailResponseDto getStoreDetail(Long storeId) {

        // 1. 공간 로딩
        Store store = storeLoader.loadStore(storeId);

        // 2. (선택) 조회 가능한 상태인지 체크
        storeValidator.validateAvailable(store);

        // 3. Floor 로딩
        Floor floor = store.getFloorId();

        // 4. DTO로 변환
        return storeMapper.toDetailDto(store, floor);
    }

}
