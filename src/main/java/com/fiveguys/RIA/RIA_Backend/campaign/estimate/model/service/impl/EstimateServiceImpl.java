package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.PermissionValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component.*;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateSpaceUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDeleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimatePageResponseDto;
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
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.EstimateErrorCode;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProposalErrorCode;
import com.fiveguys.RIA.RIA_Backend.common.util.PipelinePolicy;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    private final PermissionValidator permissionValidator;
    private final PipelinePolicy pipelinePolicy;



    @Override
    @Transactional
    public EstimateCreateResponseDto createEstimate(EstimateCreateRequestDto dto, Long userId) {

        // 1. 요청값 검증
        estimateValidator.validateCreate(dto);

        // 2. 작성자(로그인 유저)
        User createdUser = estimateLoader.loadUser(userId);

        // 3. 프로젝트 & 파이프라인
        Project project = null;
        Pipeline pipeline = null;

        if (dto.getProjectId() != null) {
            project = estimateLoader.loadProjectWithPipeline(dto.getProjectId());
            pipeline = project.getPipeline();

            if (pipeline == null) {
                throw new CustomException(EstimateErrorCode.PIPELINE_NOT_FOUND);
            }
        }

        // 4. 기타 연관 엔티티
        Client client = estimateLoader.loadClient(dto.getClientId());
        ClientCompany company = estimateLoader.loadCompany(dto.getClientCompanyId());
        Proposal proposal = dto.getProposalId() != null
                ? estimateLoader.loadProposal(dto.getProposalId())
                : null;

        // 5. Estimate 엔티티 생성
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

        // 6. 공간 스냅샷 저장
        long totalAmount = 0;
        for (EstimateSpaceRequestDto spaceDto : dto.getSpaces()) {
            Store store = estimateLoader.loadStore(spaceDto.getStoreId());
            StoreEstimateMap map = storeEstimateMapMapper.toEntity(store, estimate, spaceDto);
            storeEstimateMapRepository.save(map);

            totalAmount += map.getFinalEstimateAmount();
        }

        // 7. Pipeline 단계 자동 이동
        if (pipeline != null) {
            pipelinePolicy.handleEstimateCreated(pipeline, project);
        }

        // 8. 응답
        return EstimateCreateResponseDto.builder()
                .estimateId(estimate.getEstimateId())
                .projectId(project != null ? project.getProjectId() : null)
                .pipelineId(pipeline != null ? pipeline.getPipelineId() : null)
                .totalSpaces(dto.getSpaces().size())
                .totalAmount(totalAmount)
                .createdAt(estimate.getCreatedAt())
                .build();
    }



// 견적 목록 조회
@Override
@Transactional(readOnly = true)
public EstimatePageResponseDto<EstimateListResponseDto> getEstimateList(
        Long projectId,
        Long clientCompanyId,
        String keyword,
        Estimate.Status status,
        int page,
        int size
) {

    Pageable pageable = PageRequest.of(page - 1, size);

    // 취소됨 필터 숨김 로직
    Estimate.Status excludeCanceled =
            (status == null || status != Estimate.Status.CANCELED)
                    ? Estimate.Status.CANCELED
                    : null;

    Page<EstimateListResponseDto> result =
            estimateRepository.findEstimateList(
                    projectId,
                    clientCompanyId,
                    keyword,
                    status,
                    excludeCanceled,
                    pageable
            );

    return EstimatePageResponseDto.<EstimateListResponseDto>builder()
            .page(page)
            .size(size)
            .totalCount(result.getTotalElements())
            .data(result.getContent())
            .build();
}
    @Override
    @Transactional(readOnly = true)
    public EstimateDetailResponseDto getEstimateDetail(Long estimateId) {
        Estimate estimate = estimateLoader.loadEstimateDetail(estimateId);

        return estimateMapper.toDetailDto(estimate);
    }

    @Override
    @Transactional
    public EstimateDeleteResponseDto deleteEstimate(Long estimateId, CustomUserDetails user) {

        // 1. 로딩
        Estimate estimate = estimateLoader.loadEstimate(estimateId);

        // 2. 권한 체크 (작성자 / 팀장 / 관리자만)
        permissionValidator.validateOwnerOrLeadOrAdmin(estimate.getCreatedUser(), user);

        // 3. 상태 검증 (완료/반려는 삭제 불가)
        estimateValidator.validateDelete(estimate);

        // 4. 소프트 삭제
        estimate.cancel();   // = 상태를 CANCELED 로 변경


        return EstimateDeleteResponseDto.builder()
                .estimateId(estimateId)
                .status(estimate.getStatus().name())
                .deletedBy(user.getUserId())
                .deletedAt(LocalDateTime.now())
                .build();
    }


    @Override
    @Transactional
    public EstimateDetailResponseDto updateEstimate(
            Long estimateId,
            EstimateUpdateRequestDto dto,
            CustomUserDetails user
    ) {
        // 1. 견적 로딩
        Estimate estimate = estimateLoader.loadEstimate(estimateId);

        // 2. 권한 체크
        permissionValidator.validateOwnerOrLeadOrAdmin(estimate.getCreatedUser(), user);

        // 3. 수정 가능한 상태인지 체크
        estimateValidator.validateUpdatableStatus(estimate);

        // 4. 필드 검증
        estimateValidator.validateUpdateFields(dto);

        // 5. 변경된 고객사/고객/프로젝트 로딩
        Project newProject = dto.getProjectId() != null
                ? estimateLoader.loadProject(dto.getProjectId())
                : null;

        ClientCompany newCompany = dto.getClientCompanyId() != null
                ? estimateLoader.loadCompany(dto.getClientCompanyId())
                : null;

        Client newClient = dto.getClientId() != null
                ? estimateLoader.loadClient(dto.getClientId())
                : null;

        ClientCompany targetCompany =
                (newCompany != null) ? newCompany : estimate.getClientCompany();

        // 수정시 제목 중복 검증
        estimateValidator.validateDuplicateTitleOnUpdate(
                dto.getEstimateTitle(),
                targetCompany,
                estimateId
        );
        // 6. 고객사-고객 일치 검증
        estimateValidator.validateClientCompanyChange(
                estimate.getClient(),
                newClient,
                estimate.getClientCompany(),
                newCompany
        );

        // 7. 엔티티 업데이트
        estimate.update(
                dto.getEstimateTitle(),
                dto.getEstimateDate(),
                dto.getDeliveryDate(),
                dto.getRemark(),
                newProject,
                newCompany,
                newClient
        );

        // 8. 공간 업데이트
        estimateValidator.validateSpacesUpdate(dto.getSpaces());
        if (dto.getSpaces() != null) {
            for (EstimateSpaceUpdateRequestDto spaceDto : dto.getSpaces()) {

                StoreEstimateMap map =
                        storeEstimateMapRepository.findById(spaceDto.getStoreEstimateMapId())
                                .orElseThrow(() -> new CustomException(EstimateErrorCode.STORE_ESTIMATE_MAP_NOT_FOUND));

                // 값 변경
                map.updateSpace(
                        spaceDto.getAdditionalFee(),
                        spaceDto.getDiscountAmount(),
                        spaceDto.getDescription()
                );
            }
        }

//        // 9. 프로젝트 변경 시 파이프라인 자동 변경
//        if (newProject != null && !newProject.equals(estimate.getProject())) {
//            pipelinePolicy.handleEstimateLinked(newProject);
//            estimate.setStatus(Estimate.Status.SUBMITTED);
//        }

        // 10. 응답 생성
        return estimateMapper.toDetailDto(estimate);
    }
}
