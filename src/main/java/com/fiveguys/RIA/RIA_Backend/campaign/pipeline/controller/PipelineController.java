package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.controller;


import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.request.PipelineStageUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageUpdateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.service.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pipelines")
@Tag(name = "Pipeline", description = "영업 파이프라인 단계 관리 API")

public class PipelineController {

  private final PipelineService pipelineService;

  //파이프라인 스테이지 수동변경
  @PatchMapping("/{pipelineId}/stages")
  @Operation(
      summary = "파이프라인 단계 변경",
      description = "특정 파이프라인의 스테이지를 수동으로 변경한다."
  )
  @Parameter(
      name = "pipelineId",
      description = "파이프라인 ID",
      required = true
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "변경 성공",
          content = @Content(schema = @Schema(implementation = PipelineStageUpdateResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "파이프라인 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<PipelineStageUpdateResponseDto> updateStage(
      @PathVariable Long pipelineId,
      @RequestBody PipelineStageUpdateRequestDto requestDto,
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    PipelineStageUpdateResponseDto response =
        pipelineService.updateStage(pipelineId, requestDto, user);

    return ResponseEntity.ok(response);
  }
}
