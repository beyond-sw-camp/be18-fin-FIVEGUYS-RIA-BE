package com.fiveguys.RIA.RIA_Backend.campaign.estimate.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estimates")
public class EstimateController {

    private final EstimateService estimateService;

    // 견적 생성
    @PostMapping
    public ResponseEntity<EstimateCreateResponseDto> createEstimate(
            @RequestBody EstimateCreateRequestDto request
    ) {
        EstimateCreateResponseDto response = estimateService.createEstimate(request);
        return ResponseEntity.ok(response);
    }
}
