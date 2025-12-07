package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompleteContractRequestDto {

    private final String remark;
}
