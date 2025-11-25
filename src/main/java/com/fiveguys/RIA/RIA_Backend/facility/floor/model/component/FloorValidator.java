package com.fiveguys.RIA.RIA_Backend.facility.floor.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.FloorErrorCode;
import org.springframework.stereotype.Component;

@Component
public class FloorValidator {
    public void validateZoneId(Long zoneId) {
        if (zoneId == null || zoneId <= 0) {
            throw new CustomException(FloorErrorCode.INVALID_ZONE_ID);
        }
    }
}
