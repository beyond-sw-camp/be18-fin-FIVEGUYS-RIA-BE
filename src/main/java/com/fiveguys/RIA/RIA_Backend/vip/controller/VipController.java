package com.fiveguys.RIA.RIA_Backend.vip.controller;

import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipBrandTop5ResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipGradeStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipSalesTrendResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStoreSalesPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.service.VipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
      @Parameter(name = "page", description = "페이지 번호 (1-base)", example = "1"),
      @Parameter(name = "size", description = "페이지 크기", example = "10"),
      @Parameter(name = "grade", description = "VIP 등급", schema = @Schema(implementation = Vip.VipGrade.class)),
      @Parameter(name = "keyword", description = "검색 키워드 (이름, 메모 등)")
  })
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = VipListPageResponseDto.class))
      ),
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
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = VipGradeStatsResponseDto.class))
      ),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  public ResponseEntity<VipGradeStatsResponseDto> getVipStats() {
    return ResponseEntity.ok(vipService.getStats());
  }

  @GetMapping("/sales/stats")
  @Operation(
      summary = "VIP 매출 요약 통계 조회",
      description = "특정 연·월 기준 VIP 매출 비중, 매출 기여도 등 요약 통계를 조회한다. 연/월 미지정 시 최신 기준으로 조회한다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = VipStatsResponseDto.class))
      ),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  public VipStatsResponseDto getVipSalesStats(
      @Parameter(description = "조회 연도", example = "2024")
      @RequestParam(required = false) Integer year,
      @Parameter(description = "조회 월(1~12)", example = "12")
      @RequestParam(required = false) Integer month
  ) {
    return vipService.getVipSalesStats(year, month);
  }

  @GetMapping("/sales/brand/rank")
  @Operation(
      summary = "VIP 매출 상위 브랜드 TOP5 조회",
      description = "특정 연·월 기준 VIP 매출 기여도가 높은 브랜드 TOP5를 조회한다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = VipBrandTop5ResponseDto.class))
      ),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  public VipBrandTop5ResponseDto getVipBrandTop5(
      @Parameter(description = "조회 연도", example = "2024")
      @RequestParam(required = false) Integer year,
      @Parameter(description = "조회 월(1~12)", example = "12")
      @RequestParam(required = false) Integer month
  ) {
    return vipService.getVipBrandTop5(year, month);
  }

  @GetMapping("/sales/trend")
  @Operation(
      summary = "VIP 매출 추이 조회",
      description = "최근 6개월 또는 설정된 기간 기준 VIP 매출/비중 추세를 조회한다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = VipSalesTrendResponseDto.class))
      ),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  public VipSalesTrendResponseDto getVipSalesTrend() {
    return vipService.getVipSalesTrend();
  }

  // 매장별 VIP 매출 현황 (기본 size=5)
  @GetMapping("/sales/stores")
  @Operation(
      summary = "매장별 VIP 매출 현황 조회",
      description = "특정 연·월 기준 매장 단위 VIP 매출 현황을 페이징 조회한다. 기본 페이지 크기는 5이다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = VipStoreSalesPageResponseDto.class))
      ),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  public VipStoreSalesPageResponseDto getVipStoreSalesPage(
      @Parameter(description = "조회 연도", example = "2024")
      @RequestParam(required = false) Integer year,
      @Parameter(description = "조회 월(1~12)", example = "12")
      @RequestParam(required = false) Integer month,
      @Parameter(description = "페이지 번호 (0-base)", example = "0")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "페이지 크기", example = "5")
      @RequestParam(defaultValue = "5") int size
  ) {
    return vipService.getVipStoreSalesPage(year, month, page, size);
  }
}
