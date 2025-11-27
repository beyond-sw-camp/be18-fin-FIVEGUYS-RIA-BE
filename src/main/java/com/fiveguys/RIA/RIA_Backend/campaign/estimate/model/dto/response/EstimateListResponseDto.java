package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EstimateListResponseDto {

    private Long estimateId;
    private String estimateTitle;
    private String clientCompanyName;
    private String clientName;
    private String createdUserName;
    private LocalDate estimateDate;
    private Estimate.Status status;
}
