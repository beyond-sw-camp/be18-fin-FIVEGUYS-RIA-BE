package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDeleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimatePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;

public interface EstimateService {

    EstimateCreateResponseDto createEstimate(EstimateCreateRequestDto dto);

    EstimatePageResponseDto<EstimateListResponseDto> getEstimateList(
            Long projectId,
            Long clientCompanyId,
            String keyword,
            Estimate.Status status,
            int page,
            int size
    );

    EstimateDetailResponseDto getEstimateDetail(Long estimateId);

    EstimateDeleteResponseDto deleteEstimate(Long estimateId, CustomUserDetails user);

}
