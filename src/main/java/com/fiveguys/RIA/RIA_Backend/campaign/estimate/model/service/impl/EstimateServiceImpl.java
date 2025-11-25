package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component.EstimateLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component.EstimateMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component.EstimateValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate.PaymentCondition;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate.Status;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository.EstimateRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service.EstimateService;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EstimateServiceImpl implements EstimateService {

    private final EstimateRepository estimateRepository;
    private final EstimateLoader estimateLoader;
    private final EstimateValidator estimateValidator;
    private final EstimateMapper estimateMapper;

    @Override
    public EstimateCreateResponseDto createEstimate(EstimateCreateRequestDto dto) {

        // 1. 입력 검증
        estimateValidator.validateCreate(dto);
        estimateValidator.validatePrices(
                dto.getBasePrice(),
                dto.getAdditionalPrice(),
                dto.getDiscountPrice()
        );
        estimateValidator.validateDates(dto.getEstimateDate(), dto.getDeliveryDate());

        // 2. 엔티티 로딩 (project, pipeline 은 옵션)
        Project project = estimateLoader.loadProject(dto.getProjectId());
        Pipeline pipeline = estimateLoader.loadPipeline(dto.getPipelineId());
        User createdUser = estimateLoader.loadUser(dto.getCreatedUserId());
        Client client = estimateLoader.loadClient(dto.getClientId());
        ClientCompany company = estimateLoader.loadCompany(dto.getClientCompanyId());
        Store store = estimateLoader.loadStore(dto.getStoreId());

        // 3. 결제 조건 Enum 변환
        PaymentCondition paymentCondition =
                PaymentCondition.valueOf(dto.getPaymentCondition().toUpperCase());

        // 4. Estimate 생성
        Estimate estimate = Estimate.create(
                project,
                pipeline,
                createdUser,
                client,
                company,
                store,
                dto.getTitle(),
                dto.getBasePrice(),
                dto.getAdditionalPrice(),
                dto.getDiscountPrice(),
                dto.getEstimateDate(),
                dto.getDeliveryDate(),
                paymentCondition,
                dto.getRemark(),
                Status.DRAFT
        );

        // 5. 저장
        estimateRepository.save(estimate);

        // 6. 응답 DTO 변환
        return estimateMapper.toCreateDto(estimate);
    }
}
