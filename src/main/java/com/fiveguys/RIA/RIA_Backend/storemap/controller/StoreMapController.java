package com.fiveguys.RIA.RIA_Backend.storemap.controller;

import com.fiveguys.RIA.RIA_Backend.storemap.model.dto.StoreBasicInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.dto.StoreDetailStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.service.StoreMapBasicService;
import com.fiveguys.RIA.RIA_Backend.storemap.model.service.StoreMapDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreMapController {
    private final StoreMapBasicService basicService;
    private final StoreMapDetailService detailService;

    @GetMapping("/names")
    public List<StoreBasicInfoResponseDto> getAllStoreBasicInfo() {
        return basicService.getAllBasicInfo();
    }

    @GetMapping("/{storeId}/details")
    public StoreDetailStatsResponseDto getDetail(@PathVariable Long storeId) {
        return detailService.getStoreDetail(storeId);
    }
}
