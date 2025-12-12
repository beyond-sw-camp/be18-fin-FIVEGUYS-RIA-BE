package com.fiveguys.RIA.RIA_Backend.storemap.model.service;

import com.fiveguys.RIA.RIA_Backend.storemap.model.dto.StoreBasicInfoResponseDto;

import java.util.List;

public interface StoreMapBasicService {
    List<StoreBasicInfoResponseDto> getAllBasicInfo();
}