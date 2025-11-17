package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.controller;


import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.request.PipelineStageUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageUpdateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.service.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pipelines")
public class PipelineController {

  private final PipelineService pipelineService;

  //파이프라인 스테이지 수동변경
  @PatchMapping("/{pipelineId}/stages")
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
