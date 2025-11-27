package com.fiveguys.RIA.RIA_Backend.campaign.estimate.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimatePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
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

    // 견적 조회
    @GetMapping
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



    @GetMapping("/{estimateId}")
    public ResponseEntity<EstimateDetailResponseDto> getEstimateDetail(
            @PathVariable Long estimateId
    ) {
        return ResponseEntity.ok(
                estimateService.getEstimateDetail(estimateId)
        );
    }
}
