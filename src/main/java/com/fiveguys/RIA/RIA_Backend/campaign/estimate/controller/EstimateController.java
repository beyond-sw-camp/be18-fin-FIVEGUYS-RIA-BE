package com.fiveguys.RIA.RIA_Backend.campaign.estimate.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDeleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimatePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estimates")
@Tag(name = "Estimate", description = "견적서 관리 API")
public class EstimateController {

    private final EstimateService estimateService;

    @PostMapping
    @Operation(
            summary = "견적서 생성",
            description = "프로젝트/고객사/고객 담당자/매장 정보 기반으로 새로운 견적서를 생성한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "견적 생성 성공",
                    content = @Content(schema = @Schema(implementation = EstimateCreateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<EstimateCreateResponseDto> createEstimate(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody EstimateCreateRequestDto request
    ) {
        EstimateCreateResponseDto response =
                estimateService.createEstimate(request, user.getUserId());

        return ResponseEntity.ok(response);
    }

    // 견적 조회
    @GetMapping
    @Operation(
            summary = "견적서 목록 조회",
            description = "프로젝트/고객사/키워드/상태 기반으로 견적서 목록을 조회한다."
    )
    @Parameters({
            @Parameter(name = "project_id", description = "프로젝트 ID"),
            @Parameter(name = "client_company_id", description = "고객사 ID"),
            @Parameter(name = "keyword", description = "검색 키워드"),
            @Parameter(name = "status", description = "견적 상태", schema = @Schema(implementation = Estimate.Status.class)),
            @Parameter(name = "page", description = "페이지 번호 (기본값 1)", example = "1"),
            @Parameter(name = "size", description = "페이지 크기 (기본값 20)", example = "20")
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = EstimatePageResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })

    public ResponseEntity<EstimatePageResponseDto<EstimateListResponseDto>> getEstimates(
            @RequestParam(required = false) Long project_id,
            @RequestParam(required = false) Long client_company_id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Estimate.Status status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        EstimatePageResponseDto<EstimateListResponseDto> response =
                estimateService.getEstimateList(
                        project_id,
                        client_company_id,
                        keyword,
                        status,
                        page,
                        size
                );

        return ResponseEntity.ok(response);
    }


    // 견적 상세 조회
    @GetMapping("/{estimateId}")
    @Operation(
            summary = "견적서 상세 조회",
            description = "견적 ID로 견적서 상세 정보를 조회한다."
    )
    @Parameter(name = "estimateId", description = "견적 ID", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = EstimateDetailResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "해당 견적 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<EstimateDetailResponseDto> getEstimateDetail(
            @PathVariable Long estimateId
    ) {
        return ResponseEntity.ok(
                estimateService.getEstimateDetail(estimateId)
        );
    }

    // 견적 삭제
    @DeleteMapping("/{estimateId}")
    @Operation(
            summary = "견적서 삭제",
            description = "견적서을 삭제한다. 관리자 또는 작성자만 삭제 가능."
    )
    @Parameter(name = "estimateId", description = "삭제할 견적 ID", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(schema = @Schema(implementation = EstimateDeleteResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "견적 없음")
    })
    public ResponseEntity<EstimateDeleteResponseDto> deleteEstimate(
            @PathVariable Long estimateId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        EstimateDeleteResponseDto response =
                estimateService.deleteEstimate(estimateId, user);

        return ResponseEntity.ok(response);
    }

    // 견적 수정 (PATCH)
    @PatchMapping("/{estimateId}")
    @Operation(
            summary = "견적서 수정",
            description = "견적서 정보를 수정한다. 담당자/리드/관리자만 수정 가능."
    )
    @Parameter(name = "estimateId", description = "수정할 견적 ID", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = EstimateDetailResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "견적 없음")
    })
    public ResponseEntity<EstimateDetailResponseDto> updateEstimate(
            @PathVariable Long estimateId,
            @RequestBody EstimateUpdateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        EstimateDetailResponseDto response =
                estimateService.updateEstimate(estimateId, dto, user);

        return ResponseEntity.ok(response);
    }
}
