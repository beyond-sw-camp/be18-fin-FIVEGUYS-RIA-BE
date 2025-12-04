package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractCompleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailSpaceResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateDetailSpaceResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.CreateContractResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContractMapper {

    private final StoreContractMapMapper storeContractMapMapper;

    public Contract toEntity(
            CreateContractRequestDto dto,
            User createdUser,
            Project project,
            Pipeline pipeline,
            Client client,
            ClientCompany clientCompany,
            Estimate estimate
    ) {
        return Contract.builder()
                .estimate(estimate)
                .project(project)
                .pipeline(pipeline)
                .client(client)
                .clientCompany(clientCompany)
                .createdUser(createdUser)
                .contractTitle(dto.getContractTitle())
                .contractType(dto.getContractType())
                .contractAmount(dto.getContractAmount())
                .commissionRate(dto.getCommissionRate())
                .paymentCondition(Contract.PaymentCondition.valueOf(dto.getPaymentCondition()))
                .status(Contract.Status.DRAFT)
                .currency(dto.getCurrency())
                .contractStartDate(dto.getContractStartDate())
                .contractEndDate(dto.getContractEndDate())
                .contractDate(dto.getContractDate())
                .totalAmount(0L)
                .build();
    }

    public ContractEstimateResponseDto toContractEstimateResponseDto(Estimate estimate) {
        if (Objects.isNull(estimate)) {
            return null;
        }

        long totalAmount = estimate.getStoreEstimateMaps().stream()
                .mapToLong(se -> se.getFinalEstimateAmount())
                .sum();

        return ContractEstimateResponseDto.builder()
                .estimateId(estimate.getEstimateId())
                .estimateTitle(estimate.getEstimateTitle())
                .clientCompanyName(estimate.getClientCompany().getCompanyName())
                .clientName(estimate.getClient().getName())
                .estimateDate(estimate.getEstimateDate())
                .totalAmount(totalAmount)
                .status(estimate.getStatus())
                .build();
    }

    public ContractEstimateDetailResponseDto toContractEstimateDetailResponseDto(Estimate estimate) {

        // 1. 공간별 견적 정보 변환
        List<ContractEstimateDetailSpaceResponseDto> spaces = estimate.getStoreEstimateMaps().stream()
                .map(map -> ContractEstimateDetailSpaceResponseDto.builder()
                        .storeEstimateMapId(map.getStoreEstimateMapId())
                        .storeNumber(map.getStore().getStoreNumber())
                        .areaSize(map.getAreaSize())
                        .rentPrice(map.getRentPrice())
                        .additionalFee(map.getAdditionalFee())
                        .discountAmount(map.getDiscountAmount())
                        .finalEstimateAmount(map.getFinalEstimateAmount())
                        .description(map.getDescription())
                        .build()
                )
                .toList();

        // 2. DTO 빌드 후 반환
        return ContractEstimateDetailResponseDto.builder()
                .estimateId(estimate.getEstimateId())
                .estimateTitle(estimate.getEstimateTitle())
                .estimateDate(estimate.getEstimateDate())
                .clientCompanyName(estimate.getClientCompany().getCompanyName())
                .clientName(estimate.getClient().getName())
                .paymentCondition(estimate.getPaymentCondition())
                .remark(estimate.getRemark())   // remark -> note
                .spaces(spaces)
                .build();
    }

    public CreateContractResponseDto toCreateResponseDto(Contract contract) {
        return CreateContractResponseDto.builder()
                .contractId(contract.getContractId())
                .totalAmount(contract.getTotalAmount())
                .status(contract.getStatus())
                .createdAt(contract.getCreatedAt())
                .createUserId(contract.getCreatedUser().getId())
                .contractStartDate(contract.getContractStartDate())
                .contractEndDate(contract.getContractEndDate())
                .build();
    }

    public <T> ContractPageResponseDto<T> toPageResponseDto(
            int page,
            int size,
            long totalCount,
            List<T> data
    ) {
        return ContractPageResponseDto.<T>builder()
                .page(page)
                .size(size)
                .totalCount(totalCount)
                .data(data)
                .build();
    }

    public ContractDetailResponseDto toDetailResponseDto(Contract contract) {

        List<ContractDetailSpaceResponseDto> spaces = contract.getStoreContractMaps().stream()
                .map(storeContractMapMapper::toResponseDto)
                .toList();

        return ContractDetailResponseDto.builder()
                .contractId(contract.getContractId())
                .projectId(contract.getProject().getProjectId())
                .pipelineId(contract.getPipeline().getPipelineId())
                .clientCompanyId(contract.getClientCompany().getId())
                .clientCompanyName(contract.getClientCompany().getCompanyName())
                .clientId(contract.getClient().getId())
                .clientName(contract.getClient().getName())
                .estimateId(contract.getEstimate() != null ? contract.getEstimate().getEstimateId() : null)
                .contractTitle(contract.getContractTitle())
                .commissionRate(contract.getCommissionRate())
                .totalAmount(contract.getTotalAmount())
                .contractStartDate(contract.getContractStartDate())
                .contractEndDate(contract.getContractEndDate())
                .contractDate(contract.getContractDate())
                .paymentCondition(contract.getPaymentCondition())
                .status(contract.getStatus())
                .createUserId(contract.getCreatedUser().getId())
                .createUserName(contract.getCreatedUser().getName())
                .createdAT(contract.getCreatedAt())
                .updatedAt(contract.getUpdatedAt())
                .spaces(spaces)
                .build();
    }

    public ContractCompleteResponseDto toCompleteResponseDto(
            Contract contract,
            Proposal proposal,
            Estimate estimate,
            Revenue revenue,
            List<StoreTenantMap> tenants
    ) {
        ContractCompleteResponseDto.RelatedRecords related = toRelatedRecords(proposal, estimate, revenue, tenants);

        return ContractCompleteResponseDto.builder()
                .contractId(contract.getContractId())
                .status(contract.getStatus())
                .relatedRecords(related)
                .updatedAt(contract.getUpdatedAt())
                .build();
    }

    private ContractCompleteResponseDto.RelatedRecords toRelatedRecords(
            Proposal proposal,
            Estimate estimate,
            Revenue revenue,
            List<StoreTenantMap> tenantList
    ) {
        List<ContractCompleteResponseDto.RelatedRecords.StoreRecord> storeRecords = tenantList.stream()
                .map(t -> ContractCompleteResponseDto.RelatedRecords.StoreRecord.builder()
                        .storeId(t.getStore().getStoreId())
                        .storeName(t.getStoreDisplayName())
                        .status(t.getStatus())
                        .build())
                .collect(Collectors.toList());

        return ContractCompleteResponseDto.RelatedRecords.builder()
                .proposalId(proposal != null ? proposal.getProposalId() : null)
                .proposalStatus(proposal != null ? proposal.getStatus() : null)
                .estimateId(estimate != null ? estimate.getEstimateId() : null)
                .estimateStatus(estimate != null ? estimate.getStatus() : null)
                .revenueId(revenue != null ? revenue.getRevenueId() : null)
                .revenueStatus(revenue != null ? revenue.getStatus() : null)
                .stores(storeRecords)
                .build();
    }
}