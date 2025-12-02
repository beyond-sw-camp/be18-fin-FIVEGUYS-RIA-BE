package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateStoreMapResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateSummaryDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.StoreEstimateMap;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstimateMapper {

    public Estimate toEntity(
            Project project,
            Pipeline pipeline,
            User createdUser,
            Proposal proposal,
            Client client,
            ClientCompany company,
            EstimateCreateRequestDto dto
    ) {
        return Estimate.builder()
                .project(project)
                .pipeline(pipeline)
                .createdUser(createdUser)
                .client(client)
                .clientCompany(company)
                .proposal(proposal)
                .estimateTitle(dto.getTitle())
                .estimateDate(dto.getEstimateDate())
                .deliveryDate(dto.getDeliveryDate())
                .paymentCondition(
                        Estimate.PaymentCondition.valueOf(dto.getPaymentCondition())
                )
                .remark(dto.getRemark())
                .status(project != null ? Estimate.Status.SUBMITTED : Estimate.Status.DRAFT)
                .build();
    }



    public EstimateCreateResponseDto toCreateDto(
            Estimate estimate,
            int totalSpaces,
            long totalAmount
    ) {
        return EstimateCreateResponseDto.builder()
                .estimateId(estimate.getEstimateId())

                .totalSpaces(totalSpaces)
                .totalAmount(totalAmount)
                .createdAt(estimate.getCreatedAt())
                .build();
    }

    public EstimateDetailResponseDto toDetailDto(Estimate estimate) {

        List<EstimateStoreMapResponseDto> spaces =
                estimate.getStoreEstimateMaps().stream()
                        .map(map -> EstimateStoreMapResponseDto.builder()
                                .storeEstimateMapId(map.getStoreEstimateMapId())
                                .floorId(map.getStore().getFloorId().getFloorId())
                                .storeId(map.getStore().getStoreId())

                                // 층, 매장명
                                .floorName(map.getStore().getFloorId().getFloorName().name())
                                .storeName(map.getStore().getStoreNumber())

                                // 임대료: StoreEstimateMap에서 가져오기
                                .baseAmount(map.getRentPrice())
                                .rentFee(map.getRentPrice())

                                // 면적: StoreEstimateMap 값 사용
                                .area(map.getAreaSize())

                                // 사용자 입력값 반영해야 하는 필드들
                                .additionalFee(map.getAdditionalFee())
                                .discountAmount(map.getDiscountAmount())
                                .finalAmount(map.getFinalEstimateAmount())
                                .remark(map.getDescription())

                                .build()
                        )
                        .toList();

        return EstimateDetailResponseDto.builder()
                .estimateId(estimate.getEstimateId())
                .estimateTitle(estimate.getEstimateTitle())
                .projectId(estimate.getProject() != null ? estimate.getProject().getProjectId() : null)
                .projectTitle(estimate.getProject() != null ? estimate.getProject().getTitle() : "연결된 프로젝트 없음")
                .proposalId(estimate.getProposal() != null ? estimate.getProposal().getProposalId() : null)
                .proposalTitle(estimate.getProposal() != null ? estimate.getProposal().getTitle() : "연결된 제안 없음")
                .clientCompanyName(estimate.getClientCompany().getCompanyName())
                .clientName(estimate.getClient().getName())
                .createdUserName(estimate.getCreatedUser().getName())
                .estimateDate(estimate.getEstimateDate())
                .deliveryDate(estimate.getDeliveryDate())
                .paymentCondition(estimate.getPaymentCondition())
                .remark(estimate.getRemark())
                .status(estimate.getStatus())
                .spaces(spaces)
                .build();
    }
    public EstimateSummaryDto toSummaryDto(Estimate estimate) {

        long totalAmount = estimate.getStoreEstimateMaps().stream()
                .mapToLong(StoreEstimateMap::getFinalEstimateAmount)
                .sum();

        return EstimateSummaryDto.builder()
                .estimateId(estimate.getEstimateId())
                .title(estimate.getEstimateTitle())

                .writerName(estimate.getCreatedUser().getName())
                .createdDate(estimate.getCreatedAt().toLocalDate())
                .totalAmount(totalAmount)
                .remark(estimate.getRemark())
                .build();
    }

}
