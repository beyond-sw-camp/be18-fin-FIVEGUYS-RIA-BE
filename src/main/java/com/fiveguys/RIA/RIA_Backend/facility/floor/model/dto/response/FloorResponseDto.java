package com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FloorResponseDto {
    private Long floorId;
    private String floorName;   // B1, F1 ...
    private String category;    // HIGH_JEWELRY_WATCH ...
    private String createdAt;
    private String updatedAt;
}
