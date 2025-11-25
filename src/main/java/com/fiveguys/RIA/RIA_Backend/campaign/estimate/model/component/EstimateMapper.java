package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class EstimateMapper {

    public EstimateCreateResponseDto toCreateDto(Estimate e) {
        return EstimateCreateResponseDto.builder()
                .estimateId(e.getEstimateId())
                .title(e.getTitle())
                .status(e.getStatus().name())
                .totalPrice(e.getTotalPrice())
                .createdAt(e.getCreatedAt())
                .build();
    }

}
