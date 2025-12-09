package com.fiveguys.RIA.RIA_Backend.campaign.revenue.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.DailySalesHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueListItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenuePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueSettlementHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.RevenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/revenues")
@RequiredArgsConstructor
@Tag(name = "매출 · 정산", description = "매출(정산) 카드, 상세, 정산/일매출 히스토리 조회 API")
public class RevenueController {

  private final RevenueService revenueService;

  @Operation(
      summary = "매출(정산) 목록 조회",
      description = """
          매출(정산) 카드 목록을 페이지네이션과 필터 조건으로 조회한다.

          - storeType : STORE.type (예: REGULAR, POPUP)
          - creatorId : REVENUE.created_user (담당자 ID)
          - page      : 0-base 페이지 인덱스
          - size      : 페이지당 카드 수
          """,
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "매출(정산) 목록 조회 성공",
              content = @Content(
                  schema = @Schema(implementation = RevenuePageResponseDto.class)
              )
          )
      }
  )
  @GetMapping
  public ResponseEntity<RevenuePageResponseDto<RevenueListItemResponseDto>> getRevenueList(
      @Parameter(description = "0-base 페이지 인덱스", example = "0")
      @RequestParam(defaultValue = "0") int page,

      @Parameter(description = "페이지당 카드 수", example = "12")
      @RequestParam(defaultValue = "12") int size,

      @Parameter(
          description = "매장 유형 필터 (STORE.type). 예: REGULAR, POPUP",
          example = "REGULAR"
      )
      @RequestParam(name = "storeType", required = false) String storeType,

      @Parameter(
          description = "매출 생성 담당자 ID (REVENUE.created_user)",
          example = "1"
      )
      @RequestParam(name = "creatorId", required = false) Long creatorId
  ) {
    Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(
        revenueService.getRevenueList(storeType, creatorId, pageable)
    );
  }

  @Operation(
      summary = "매출(정산) 상세 조회",
      description = """
          단일 매출(정산) 건의 상세 정보를 조회한다.

          - revenueId       : 매출(정산) PK
          - storeTenantMapId: 동일 계약에 여러 매장이 연결된 경우 특정 매장 기준으로 상세 조회할 때 사용 (옵션)
          """,
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "매출(정산) 상세 조회 성공",
              content = @Content(
                  schema = @Schema(implementation = RevenueDetailResponseDto.class)
              )
          ),
          @ApiResponse(responseCode = "404", description = "해당 ID의 매출(정산)을 찾을 수 없음")
      }
  )
  @GetMapping("/{revenueId}")
  public ResponseEntity<RevenueDetailResponseDto> getRevenueDetail(
      @Parameter(description = "매출(정산) ID", example = "1")
      @PathVariable Long revenueId,

      @Parameter(
          description = "매장-임차 맵핑 ID (스토어 기준 상세 조회가 필요한 경우에만 전달)",
          example = "10"
      )
      @RequestParam(value = "storeTenantMapId", required = false) Long storeTenantMapId
  ) {
    RevenueDetailResponseDto dto = revenueService.getRevenueDetail(revenueId, storeTenantMapId);
    return ResponseEntity.ok(dto);
  }

  @Operation(
      summary = "계약별 정산 히스토리 조회",
      description = """
          특정 계약에 대한 정산 이력을 기간 기준으로 조회한다.

          - 계약 ID 기준으로 RevenueSettlement / Revenue 와 연결된 정산 금액, 기간 등을 응답한다.
          - 조회 구간: [startYear-startMonth] ~ [endYear-endMonth] (양 끝 포함)
          """,
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "정산 히스토리 조회 성공",
              content = @Content(
                  schema = @Schema(implementation = RevenueSettlementHistoryResponseDto.class)
              )
          )
      }
  )
  @GetMapping("/contracts/{contractId}/settlements")
  public RevenueSettlementHistoryResponseDto getSettlementHistory(
      @Parameter(description = "계약 ID", example = "1")
      @PathVariable Long contractId,

      @Parameter(description = "조회 시작 연도", example = "2023")
      @RequestParam int startYear,

      @Parameter(description = "조회 시작 월(1~12)", example = "1")
      @RequestParam int startMonth,

      @Parameter(description = "조회 종료 연도", example = "2024")
      @RequestParam int endYear,

      @Parameter(description = "조회 종료 월(1~12)", example = "12")
      @RequestParam int endMonth
  ) {
    return revenueService.getSettlementHistoryByContractAndPeriod(
        contractId,
        startYear,
        startMonth,
        endYear,
        endMonth
    );
  }

  @Operation(
      summary = "매장 일별 매출 히스토리 조회",
      description = """
          특정 매장(STORE_TENANT_MAP 기준)의 일별 매출 히스토리를 조회한다.

          - storeTenantMapId : 매장-임차 맵핑 ID
          - startDate ~ endDate : 일자 구간 (YYYY-MM-DD, 양 끝 포함)
          """,
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "일별 매출 히스토리 조회 성공",
              content = @Content(
                  schema = @Schema(implementation = DailySalesHistoryResponseDto.class)
              )
          )
      }
  )
  @GetMapping("/stores/{storeTenantMapId}/sales/daily")
  public DailySalesHistoryResponseDto getDailySalesHistory(
      @Parameter(description = "매장-임차 맵핑 ID", example = "10")
      @PathVariable Long storeTenantMapId,

      @Parameter(description = "조회 시작 일자 (YYYY-MM-DD)", example = "2024-01-01")
      @RequestParam
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate startDate,

      @Parameter(description = "조회 종료 일자 (YYYY-MM-DD)", example = "2024-01-31")
      @RequestParam
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate endDate
  ) {
    return revenueService.getDailySalesHistory(
        storeTenantMapId,
        startDate,
        endDate
    );
  }
}
