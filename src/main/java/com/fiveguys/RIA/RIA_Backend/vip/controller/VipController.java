package com.fiveguys.RIA.RIA_Backend.vip.controller;

import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.service.VipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vip")
@Tag(name = "VIP", description = "VIP 고객 관리 API")

public class VipController {

  private final VipService vipService;

  @GetMapping
  @Operation(
      summary = "VIP 목록 조회",
      description = "등급/키워드 기반 VIP 목록을 페이징 조회한다."
  )
  @Parameters({
      @Parameter(name = "page", description = "페이지 번호", example = "1"),
      @Parameter(name = "size", description = "페이지 크기", example = "10"),
      @Parameter(name = "grade", description = "VIP 등급", schema = @Schema(implementation = Vip.VipGrade.class)),
      @Parameter(name = "keyword", description = "검색 키워드")
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = VipListPageResponseDto.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  public ResponseEntity<VipListPageResponseDto> getVipList(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "grade", required = false) Vip.VipGrade grade,
      @RequestParam(value = "keyword", required = false) String keyword

  ) {
    return ResponseEntity.ok(vipService.getVipList(page, size, grade, keyword));
  }

  // 등급조회
  @GetMapping("/stats")
  @Operation(
      summary = "VIP 통계 조회",
      description = "VIP 총합 및 등급별 통계를 조회한다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = VipStatsResponseDto.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  public ResponseEntity<VipStatsResponseDto> getVipStats() {
    return ResponseEntity.ok(vipService.getStats());
  }
}
