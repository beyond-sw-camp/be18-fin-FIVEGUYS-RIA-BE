package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.StoreEstimateMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class EstimateDetailResponseDto {

    private Long estimateId;
    private String estimateTitle;

    private String clientCompanyName;
    private String clientName;
    private String createdUserName;

    private LocalDate estimateDate;
    private LocalDate deliveryDate;
    private Estimate.PaymentCondition paymentCondition;
    private String remark;
    private Estimate.Status status;

    private List<EstimateStoreMapResponseDto> spaces;

}
