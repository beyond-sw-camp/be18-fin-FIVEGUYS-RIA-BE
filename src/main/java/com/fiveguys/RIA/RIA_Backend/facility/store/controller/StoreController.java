package com.fiveguys.RIA.RIA_Backend.facility.store.controller;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/floors")
public class StoreController {

    private final StoreService storeService;

    /** 특정 층의 매장 목록 조회 */
    @GetMapping("/{floorId}/stores")
    public ResponseEntity<StoreListResponseDto> getStoresByFloor(
            @PathVariable("floorId") Long floorId
    ) {
        StoreListResponseDto response = storeService.getStoresByFloor(floorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{floorId}/spaces")
    public ResponseEntity<StoreListResponseDto> getAvailableSpaces(
            @PathVariable Long floorId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Store.StoreType type
    ) {
        return ResponseEntity.ok(storeService.getAvailableSpaces(floorId, keyword, type));
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreDetailResponseDto> getStoreDetail(
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(storeService.getStoreDetail(storeId));
    }
}
