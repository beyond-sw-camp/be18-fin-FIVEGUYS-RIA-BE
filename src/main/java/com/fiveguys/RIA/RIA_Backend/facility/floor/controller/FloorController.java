package com.fiveguys.RIA.RIA_Backend.facility.floor.controller;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.Service.FloorService;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response.FloorListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/floors")
public class FloorController {

    private final FloorService floorService;

    // 요청예시 api/floors?zone_id=1
    @GetMapping
    public ResponseEntity<FloorListResponseDto> getFloors(
            @RequestParam(name = "zone_id") Long zoneId
    ) {
        FloorListResponseDto response = floorService.getFloorsByZone(zoneId);
        return ResponseEntity.ok(response);
    }
}
