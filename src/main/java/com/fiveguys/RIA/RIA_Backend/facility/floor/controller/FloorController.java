package com.fiveguys.RIA.RIA_Backend.facility.floor.controller;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.Service.FloorService;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response.FloorListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/floors")
@Tag(name = "Floor", description = "층(Floor) 조회 API")
public class FloorController {

    private final FloorService floorService;

    // 요청예시 api/floors?zone_id=1
    @GetMapping
    @Operation(
            summary = "층 목록 조회",
            description = "지정된 존(zone)에 속한 전체 층(Floor) 목록을 조회한다."
    )
    @Parameters({
            @Parameter(
                    name = "zone_id",
                    description = "조회할 Zone ID",
                    required = true,
                    example = "1"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = FloorListResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 zone_id",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content
            )
    })
    public ResponseEntity<FloorListResponseDto> getFloors(
            @RequestParam(name = "zone_id") Long zoneId
    ) {
        FloorListResponseDto response = floorService.getFloorsByZone(zoneId);
        return ResponseEntity.ok(response);
    }
}
