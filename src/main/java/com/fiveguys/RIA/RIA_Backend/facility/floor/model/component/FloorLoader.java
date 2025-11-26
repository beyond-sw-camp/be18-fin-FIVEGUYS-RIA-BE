package com.fiveguys.RIA.RIA_Backend.facility.floor.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.FloorErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.repository.FloorRepository;
import com.fiveguys.RIA.RIA_Backend.facility.zone.model.entity.Zone;
import com.fiveguys.RIA.RIA_Backend.facility.zone.model.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FloorLoader {

    private final ZoneRepository zoneRepository;
    private final FloorRepository floorRepository;

    public Zone loadZone(Long zoneId) {
        return zoneRepository.findById(zoneId)
                .orElseThrow(() -> new CustomException(FloorErrorCode.INVALID_ZONE_ID));
    }

    public List<Floor> loadFloorsByZone(Zone zone) {
        List<Floor> floors = floorRepository.findFloorsByZone(zone);
        if (floors.isEmpty()) {
            throw new CustomException(FloorErrorCode.FLOOR_NOT_FOUND);
        }
        return floors;
    }

    public Floor loadFloor(Long floorId) {
        return floorRepository.findById(floorId)
                .orElseThrow(() -> new CustomException(FloorErrorCode.FLOOR_NOT_FOUND));
    }
}
