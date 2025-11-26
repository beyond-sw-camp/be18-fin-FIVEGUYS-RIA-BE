package com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response;

import lombok.*;
import static lombok.AccessLevel.*;


@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class StoreResponseDto {

    private Long storeId;
    private String storeNumber;
    private Double areaSize;
    private String status;
    private Long rentPrice;
    private String description;
}