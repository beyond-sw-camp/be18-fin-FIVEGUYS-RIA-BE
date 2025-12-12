package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateSpaceUpdateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class EstimateUpdateRequestDto {

    private String estimateTitle;
    private Long projectId;
    private Long clientCompanyId;
    private Long clientId;
    private LocalDate estimateDate;
    private LocalDate deliveryDate;
    private String paymentCondition;
    private String remark;

    private Long proposalId;

    private List<EstimateSpaceUpdateRequestDto> spaces; // 외부 DTO 사용
}