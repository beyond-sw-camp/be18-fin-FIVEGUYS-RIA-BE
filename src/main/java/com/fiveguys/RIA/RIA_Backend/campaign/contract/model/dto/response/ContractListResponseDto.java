package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ContractListResponseDto {
    // 계약 목록 조회
    private final Long contractId;
    private final String contractTitle;

    private final Long clientCompanyId;
    private final String clientCompanyName;
    private final String clientName;

    private final Long projectId;
    private final String projectTitle;

    private final LocalDate contractDate;

    private final Long totalAmount;

    private final Contract.Status status;

    private final Long createUserId;

    private final LocalDateTime createdAt;


    public ContractListResponseDto(
            Long contractId,
            String contractTitle,
            Long clientCompanyId,
            String clientCompanyName,
            String clientName,
            Long projectId,
            String projectTitle,
            LocalDate contractDate,
            Long totalAmount,
            Contract.Status status,
            Long createUserId,
            LocalDateTime createdAt
    ) {
        this.contractId = contractId;
        this.contractTitle = contractTitle;
        this.clientCompanyId = clientCompanyId;
        this.clientCompanyName = clientCompanyName;
        this.clientName = clientName;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.contractDate = contractDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createUserId = createUserId;
        this.createdAt = createdAt;
    }
}
