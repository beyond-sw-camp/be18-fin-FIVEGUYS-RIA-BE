package com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FloorListResponseDto {
    private Long zoneId;
    private List<FloorResponseDto> floors;


}
