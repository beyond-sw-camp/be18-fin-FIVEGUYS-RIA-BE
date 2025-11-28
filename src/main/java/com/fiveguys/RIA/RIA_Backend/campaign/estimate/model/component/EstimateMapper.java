package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateStoreMapResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
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
                .status(Estimate.Status.DRAFT)
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
                                .storeId(map.getStore().getStoreId())
                                .floorName(map.getStore().getFloorId().getFloorName().name())
                                .storeName(map.getStore().getStoreNumber())
                                .baseAmount(map.getRentPrice()) // StoreEstimateMap에 rentPrice 있음
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
}
