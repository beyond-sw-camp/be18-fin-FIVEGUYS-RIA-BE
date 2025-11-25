package com.fiveguys.RIA.RIA_Backend.facility.floor.model.Service.impl;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.Service.FloorService;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.component.FloorLoader;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.component.FloorMapper;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.component.FloorValidator;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response.FloorListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.zone.model.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FloorServiceImpl implements FloorService {

    private final FloorLoader loader;
    private final FloorValidator validator;
    private final FloorMapper mapper;

    @Override
    public FloorListResponseDto getFloorsByZone(Long zoneId) {

        // 1. zoneId 검증
        validator.validateZoneId(zoneId);

        // 2. Zone 조회
        Zone zone = loader.loadZone(zoneId);

        // 3. 해당 존의 층 목록 조회
        var floors = loader.loadFloorsByZone(zone);

        // 4. DTO 변환 후 반환
        return mapper.toListDto(zoneId, floors);
    }
}
