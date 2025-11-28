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
    private LocalDate estimateDate;
    private LocalDate deliveryDate;
    private String remark;

    private Long projectId;         // 변경 가능
    private Long clientCompanyId;   // 변경 가능
    private Long clientId;          // 변경 가능

    private List<EstimateSpaceUpdateRequestDto> spaces; // 외부 DTO 사용
}