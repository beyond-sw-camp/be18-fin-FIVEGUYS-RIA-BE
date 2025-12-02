package com.fiveguys.RIA.RIA_Backend.facility.store.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.FloorErrorCode;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.StoreMapErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreValidator {
    public void validateFloorId(Long floorId) {
        if (floorId == null || floorId <= 0) {
            throw new CustomException(FloorErrorCode.INVALID_FLOOR_ID);
        }
    }

    public void validateKeyword(String keyword) {
        if (keyword != null && keyword.isBlank()) {
            throw new CustomException(StoreMapErrorCode.INVALID_KEYWORD);
        }
    }

    public void validateSpacesExist(List<Store> spaces) {
        if (spaces.isEmpty()) {
            throw new CustomException(StoreMapErrorCode.NO_AVAILABLE_SPACE);
        }
    }

    public void validateAvailable(Store store) {
        if (store.getStatus() != Store.StoreStatus.AVAILABLE) {
            throw new CustomException(StoreMapErrorCode.SPACE_NOT_AVAILABLE);
        }
    }
}
