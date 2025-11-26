package com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response;


import lombok.*;
import static lombok.AccessLevel.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class StoreListResponseDto {
    private Long floorId;
    private String floorName;
    private List<StoreResponseDto> stores;
}
