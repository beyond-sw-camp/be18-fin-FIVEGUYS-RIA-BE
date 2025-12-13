package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractProjectResponseDto {
    private final Long projectId;

    private final String projectTitle;
}
