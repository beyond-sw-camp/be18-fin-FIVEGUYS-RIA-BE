package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component.*;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.StoreEstimateMap;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository.EstimateRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository.StoreEstimateMapRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service.EstimateService;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
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
    private final StoreEstimateMapRepository storeEstimateMapRepository;

    private final EstimateLoader estimateLoader;
    private final EstimateValidator estimateValidator;
    private final EstimateMapper estimateMapper;
    private final StoreEstimateMapMapper storeEstimateMapMapper;

    @Override
    public EstimateCreateResponseDto createEstimate(EstimateCreateRequestDto dto) {

        // 1. 요청값 검증
        estimateValidator.validateCreate(dto);

        // 2. 연관 엔티티 로딩
        Project project = estimateLoader.loadProject(dto.getProjectId());
        Pipeline pipeline = estimateLoader.loadPipeline(dto.getPipelineId());
        User createdUser = estimateLoader.loadUser(dto.getCreatedUserId());
        Client client = estimateLoader.loadClient(dto.getClientId());
        ClientCompany company = estimateLoader.loadCompany(dto.getClientCompanyId());
        Proposal proposal = estimateLoader.loadProposal(dto.getProposalId());

        // 3. Estimate 저장
        Estimate estimate = estimateMapper.toEntity(
                project,
                pipeline,
                createdUser,
                proposal,
                client,
                company,
                dto
        );
        estimateRepository.save(estimate);

        // 4. 공간 목록 스냅샷 저장
        long totalAmount = 0;

        for (EstimateSpaceRequestDto spaceDto : dto.getSpaces()) {

            Store store = estimateLoader.loadStore(spaceDto.getStoreId());

            StoreEstimateMap map = storeEstimateMapMapper.toEntity(store, estimate, spaceDto);

            storeEstimateMapRepository.save(map);

            totalAmount += map.getFinalEstimateAmount();
        }

        // 5. 응답 반환
        return estimateMapper.toCreateDto(
                estimate,
                dto.getSpaces().size(),
                totalAmount
        );
    }
}
