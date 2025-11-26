package com.fiveguys.RIA.RIA_Backend.facility.store.model.component;

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
}
