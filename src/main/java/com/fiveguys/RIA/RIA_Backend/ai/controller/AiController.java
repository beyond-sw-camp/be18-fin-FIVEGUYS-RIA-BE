package com.fiveguys.RIA.RIA_Backend.ai.controller;

import com.fiveguys.RIA.RIA_Backend.ai.model.dto.AiResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.dto.RecommendResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "AI 추천",
        description = "VIP 고객 데이터를 기반으로 AI 추천을 생성하거나 조회하는 API"
)
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @Operation(
            summary = "AI 추천 생성",
            description = "특정 VIP 고객에 대한 AI 분석 및 추천 데이터를 생성합니다."
    )
    @PostMapping("/{vipId}/recommendations/generate")
    public ResponseEntity<RecommendResponseDto> generate(
            @Parameter(description = "AI 추천을 생성할 VIP ID", example = "5")
            @PathVariable Long vipId
    ) {
        RecommendResponseDto response = aiService.RecommendationsVip(vipId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "AI 추천 조회",
            description = "특정 VIP 고객에 대해 생성된 AI 추천 데이터를 조회합니다."
    )
    @GetMapping("/{vipId}/recommendations")
    public List<AiResponseDto> get(
            @Parameter(description = "AI 추천을 조회할 VIP ID", example = "5")
            @PathVariable Long vipId
    ) {
        return aiService.getRecommendations(vipId);
    }
}
