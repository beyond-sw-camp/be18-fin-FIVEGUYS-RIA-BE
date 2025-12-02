package com.fiveguys.RIA.RIA_Backend.storemap.controller;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Dto.StoreBasicInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Dto.StoreDetailStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Service.StoreBasicInfoService;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Service.StoreDetailStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreMapController {

    private final StoreBasicInfoService storeBasicInfoService;
    private final StoreDetailStatsService storeDetailStatsService;

    // -------------------------------
    // 1) 매장명 리스트 조회
    // -------------------------------
    @GetMapping("/names")
    public ResponseEntity<List<StoreBasicInfoResponseDto>> getStoreNames() {
        List<StoreBasicInfoResponseDto> result = storeBasicInfoService.getStoreNames();
        return ResponseEntity.ok(result);
    }

    // -------------------------------
    // 2) 매장 상세 정보 조회
    // -------------------------------
    @GetMapping("/{storeId}/detailstats")
    public ResponseEntity<StoreDetailStatsResponseDto> getStoreStats(
            @PathVariable Long storeId
    ) {
        StoreDetailStatsResponseDto response = storeDetailStatsService.getStoreDetail(storeId);
        return ResponseEntity.ok(response);
    }
}