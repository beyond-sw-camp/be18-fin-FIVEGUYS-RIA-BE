package com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response;


import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class StoreDetailResponseDto {
    private Long storeId;
    private Long floorId;
    private String floorName;
    private String storeNumber;

//    private String type;
    private Store.StoreType storeType;

    private Double areaSize;

    private Long rentPrice;

    private String description;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime  updatedAt;


}
