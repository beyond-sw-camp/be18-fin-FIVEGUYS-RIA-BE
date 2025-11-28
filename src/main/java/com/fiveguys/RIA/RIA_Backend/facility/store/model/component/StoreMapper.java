package com.fiveguys.RIA.RIA_Backend.facility.store.model.component;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreMapper {

    public StoreResponseDto toDto(Store store) {
        return StoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeNumber(store.getStoreNumber())
                .areaSize(store.getAreaSize())
                .status(store.getStatus().name())
                .rentPrice(store.getRentPrice())
                .type(store.getType().name())
                .description(store.getDescription())
                .build();
    }


    public StoreListResponseDto toListDto(Long floorId, String floorName, List<Store> stores) {
        return StoreListResponseDto.builder()
                .floorId(floorId)
                .floorName(floorName)
                .stores(stores.stream().map(this::toDto).toList())
                .build();
    }

    public StoreDetailResponseDto toDetailDto(Store store, Floor floor) {
        return StoreDetailResponseDto.builder()
                .storeId(store.getStoreId())
                .floorId(floor.getFloorId())
                .floorName(floor.getFloorName().name())
                .storeNumber(store.getStoreNumber())
                .type(store.getType().name())
                .areaSize(store.getAreaSize())
                .rentPrice(store.getRentPrice())
                .description(store.getDescription())
                .status(store.getStatus().name())
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getUpdatedAt())
                .build();
    }
}
