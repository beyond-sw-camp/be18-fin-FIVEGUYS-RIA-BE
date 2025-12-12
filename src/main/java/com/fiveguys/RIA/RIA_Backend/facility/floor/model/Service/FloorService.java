package com.fiveguys.RIA.RIA_Backend.facility.floor.model.Service;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response.FloorListResponseDto;

public interface FloorService {
    FloorListResponseDto getFloorsByZone(Long zoneId);
}
