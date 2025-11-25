package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate.Status;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate.PaymentCondition;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class EstimateMapper {

    public Estimate toEntity(
            Project project,
            Pipeline pipeline,
            User createdUser,
            Client client,
            ClientCompany company,
            Store store,
            EstimateCreateRequestDto dto
    ) {

        long total = dto.getBasePrice()
                + dto.getAdditionalPrice()
                - dto.getDiscountPrice();

        return Estimate.builder()
                .project(project)
                .pipeline(pipeline)
                .createdUser(createdUser)
                .client(client)
                .clientCompany(company)
                .store(store)
                .title(dto.getTitle())
                .estimateDate(dto.getEstimateDate())
                .deliveryDate(dto.getDeliveryDate())
                .paymentCondition(PaymentCondition.valueOf(dto.getPaymentCondition()))
                .basePrice(dto.getBasePrice())
                .additionalPrice(dto.getAdditionalPrice())
                .discountPrice(dto.getDiscountPrice())
                .remark(dto.getRemark())
                .totalPrice(total)
                .status(Status.DRAFT)
                .build();
    }

    public EstimateCreateResponseDto toCreateDto(Estimate estimate) {
        return EstimateCreateResponseDto.builder()
                .estimateId(estimate.getEstimateId())
                .title(estimate.getTitle())
                .status(estimate.getStatus().name())
                .totalPrice(estimate.getTotalPrice())
                .createdAt(estimate.getCreatedAt())
                .build();
    }

}
