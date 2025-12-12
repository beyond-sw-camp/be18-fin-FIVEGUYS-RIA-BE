package com.fiveguys.RIA.RIA_Backend.facility.store.controller;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.dto.response.StoreListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.service.StoreService;
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
@Tag(name = "Store", description = "매장(Store) / 공간(Space) 조회 API")
public class StoreController {

    private final StoreService storeService;

    /** 특정 층의 매장 목록 조회 */
    @GetMapping("/{floorId}/stores")
    @Operation(
            summary = "층별 매장 목록 조회",
            description = "특정 층(Floor)에 존재하는 모든 매장(Store) 목록을 조회한다."
    )
    @Parameter(
            name = "floorId",
            description = "조회할 층 ID",
            required = true,
            example = "3"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = StoreListResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 층(Floor)", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<StoreListResponseDto> getStoresByFloor(
            @PathVariable("floorId") Long floorId
    ) {
        StoreListResponseDto response = storeService.getStoresByFloor(floorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{floorId}/spaces")
    @Operation(
            summary = "층별 사용 가능한 공간 조회",
            description = "특정 층(Floor)에서 사용 가능한 공간(Space)을 키워드 및 타입 조건으로 조회한다."
    )
    @Parameters({
            @Parameter(
                    name = "floorId",
                    description = "조회할 층 ID",
                    required = true,
                    example = "3"
            ),
            @Parameter(
                    name = "keyword",
                    description = "검색 키워드 (매장명 등)",
                    example = "ABC"
            ),
            @Parameter(
                    name = "type",
                    description = "공간 타입 (Store.StoreType ENUM)",
                    schema = @Schema(implementation = Store.StoreType.class),
                    example = "POPUP" // 예시: POPUP, RETAIL, FNB 등
            )
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = StoreListResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 층(Floor)", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<StoreListResponseDto> getAvailableSpaces(
            @PathVariable Long floorId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Store.StoreType type
    ) {
        return ResponseEntity.ok(storeService.getAvailableSpaces(floorId, keyword, type));
    }

    @GetMapping("/stores/{storeId}")
    @Operation(
            summary = "매장 상세 조회",
            description = "이용 가능한 매장(Store)의 상세 정보를 조회한다."
    )
    @Parameter(
            name = "storeId",
            description = "조회할 매장 ID",
            required = true,
            example = "10"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = StoreDetailResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 매장(Store)", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<StoreDetailResponseDto> getStoreDetail(
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(storeService.getStoreDetail(storeId));
    }
}
