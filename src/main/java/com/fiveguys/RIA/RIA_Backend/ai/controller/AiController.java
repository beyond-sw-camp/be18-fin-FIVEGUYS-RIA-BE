package com.fiveguys.RIA.RIA_Backend.ai.controller;

import com.fiveguys.RIA.RIA_Backend.ai.model.dto.AiResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.dto.RecommendResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/{vipId}/recommendations/generate")
    public ResponseEntity<RecommendResponseDto> generate(@PathVariable Long vipId) {
        RecommendResponseDto response = aiService.RecommendationsVip(vipId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vipId}/recommendations")
    public List<AiResponseDto> get(@PathVariable Long vipId) {
        return aiService.getRecommendations(vipId);
    }
}
