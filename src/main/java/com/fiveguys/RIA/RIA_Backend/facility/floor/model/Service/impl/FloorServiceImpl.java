package com.fiveguys.RIA.RIA_Backend.facility.floor.model.Service.impl;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.Service.FloorService;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.component.FloorLoader;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.component.FloorMapper;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.component.FloorValidator;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response.FloorListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import com.fiveguys.RIA.RIA_Backend.facility.zone.model.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FloorServiceImpl implements FloorService {

    private final FloorLoader floorLoader;
    private final FloorValidator floorValidator;
    private final FloorMapper floorMapper;

    @Override
    public FloorListResponseDto getFloorsByZone(Long zoneId) {

        // 1. zoneId 검증
        floorValidator.validateZoneId(zoneId);

        // 2. Zone 조회
        Zone zone = floorLoader.loadZone(zoneId);

        // 3. 해당 존의 층 목록 조회
        List<Floor> floors = floorLoader.loadFloorsByZone(zone);

        // 4. DTO 변환 후 반환
        return floorMapper.toListDto(zoneId, floors);
    }
}
