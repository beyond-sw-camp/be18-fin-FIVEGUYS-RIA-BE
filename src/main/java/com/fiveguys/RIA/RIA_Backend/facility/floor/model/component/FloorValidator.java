package com.fiveguys.RIA.RIA_Backend.facility.floor.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.FloorErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FloorValidator {
    public void validateZoneId(Long zoneId) {
        if (zoneId == null || zoneId <= 0) {
            throw new CustomException(FloorErrorCode.INVALID_ZONE_ID);
        }
    }

    public void validateFloorsExist(List<Floor> floors) {
        if (floors.isEmpty()) {
            throw new CustomException(FloorErrorCode.FLOOR_NOT_FOUND);
        }
    }
}
