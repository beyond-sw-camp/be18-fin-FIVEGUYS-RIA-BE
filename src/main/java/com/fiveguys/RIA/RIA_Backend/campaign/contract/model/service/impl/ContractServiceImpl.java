package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component.ContractLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component.ContractMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component.ContractValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component.StoreContractMapMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.UpdateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.UpdateContractSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractCompleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDeleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.CreateContractResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.UpdateContractResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.StoreContractMapRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.service.ContractService;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.RevenueMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueRepository;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.component.StoreTenantMapMapper;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreTenantMapRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ContractErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractValidator contractValidator;
    private final ContractLoader contractLoader;
    private final ContractMapper contractMapper;
    private final ContractRepository contractRepository;
    private final StoreContractMapMapper storeContractMapMapper;
    private final StoreContractMapRepository storeContractMapRepository;
    private final StoreTenantMapMapper storeTenantMapMapper;
    private final StoreTenantMapRepository storeTenantMapRepository;
    private final RevenueMapper revenueMapper;
    private final RevenueRepository revenueRepository;

    @Override
    @Transactional
    public CreateContractResponseDto createContract(CreateContractRequestDto dto, Long userId) {
        // 1. 필수값 검증
        contractValidator.validateCreate(dto, userId);

        // 2. 연관 엔티티 로딩
        User createdUser = contractLoader.loadUser(userId);
        Project project = contractLoader.loadProject(dto.getProjectId());
        Pipeline pipeline = project.getPipeline();
        Client client = contractLoader.loadClient(dto.getClientId());
        ClientCompany clientCompany = contractLoader.loadCompany(dto.getClientCompanyId());

        Estimate estimate = null;
        if (dto.getEstimateId() != null) {
            estimate = contractValidator.validateEstimate(dto.getEstimateId());
        }

        // 3. Contract 생성
        Contract contract = contractMapper.toEntity(
                dto,
                createdUser,
                project,
                pipeline,
                client,
                clientCompany,
                estimate
        );

        contractRepository.save(contract);

        // 4. StoreContractMap 생성
        long totalAmount = 0L;

        for (CreateContractSpaceRequestDto spaceDto : dto.getSpaces()) {
            // 매장 로딩
            Store store = contractLoader.loadStore(spaceDto.getStoreId());

            // Mapper에서 DTO + Contract + Store + 견적 참조로 엔티티 변환
            StoreContractMap map = storeContractMapMapper.toEntity(spaceDto, contract, store);

            storeContractMapRepository.save(map);

            totalAmount += map.getFinalContractAmount();
        }

        // 보증금 + 계약금액 = 총 계약 금액
        contract.updateTotalAmount(totalAmount + (contract.getContractAmount() != null ? contract.getContractAmount() : 0));

        // 파이프라인 상태 변경
        if(pipeline != null) {
            pipeline.autoAdvance(
                    4,
                    Pipeline.StageName.NEGOTIATION,
                    Pipeline.Status.ACTIVE
            );
        }

        contractRepository.flush();

        return contractMapper.toCreateResponseDto(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractPageResponseDto<ContractListResponseDto> getContractList(
            Long projectId,
            Long clientCompanyId,
            String keyword,
            Contract.Status status,
            LocalDate contractDate,
            int page,
            int size,
            Long userId) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<ContractListResponseDto> result =
                contractRepository.findContractList(
                        projectId,
                        clientCompanyId,
                        keyword,
                        status,
                        contractDate,
                        pageable
                );

        return contractMapper.toPageResponseDto(
                page,
                size,
                result.getTotalElements(),
                result.getContent());
    }

    @Override
    public ContractDetailResponseDto getContractDetail(Long contractId, Long userId) {
        Contract contract = contractLoader.loadContract(contractId);
        return contractMapper.toDetailResponseDto(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractEstimateResponseDto> getEstimateList(
            Long projectId,
            Long clientCompanyId,
            String keyword,
            Estimate.Status status,
            Integer limit,
            Long userId
    ) {
        User user = contractLoader.loadUser(userId);
        Long targetUserId = contractValidator.getAccessibleUserId(user);

        // JPQL limit 지원 X -> Pagable 활용해야
        Pageable pageable = PageRequest.of(0, limit);

        List<Estimate> estimates;
        try {
            estimates = contractRepository.findContractEstimateList(
                    projectId,
                    clientCompanyId,
                    keyword,
                    status,
                    targetUserId,
                    pageable
            );
        } catch (Exception e) {
            throw new CustomException(ContractErrorCode.INTERNAL_SERVER_ERROR);
        }

        if (estimates.isEmpty()) {
            throw new CustomException(ContractErrorCode.ESTIMATE_NOT_FOUND);
        }

        return estimates.stream()
                .map(contractMapper::toContractEstimateResponseDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public ContractEstimateDetailResponseDto getEstimateDetail(Long estimateId, Long userId) {
        Estimate estimate = contractLoader.loadEstimate(estimateId);

        User user = contractLoader.loadUser(userId);

        contractValidator.validateEstimateAccess(estimate, user);

        return contractMapper.toContractEstimateDetailResponseDto(estimate);
    }

    @Override
    @Transactional
    public ContractCompleteResponseDto completeContract(Long contractId, Long userId) {
        // 1. 사용자 & 계약 로드
        User user = contractLoader.loadUser(userId);
        Contract contract = contractLoader.loadContract(contractId);

        // 2. 권한 검증
        contractValidator.validateEditPermission(contract, user);

        contractValidator.validateCompleteStatus(contract);

        // 3. 계약서 상태 검증
        contractValidator.validateCompleteStatus(contract);

        // 4. 매장 상태 및 날짜 검증
        contractValidator.validateStoresForCompletion(contract);

        // 5. 연관 엔티티 로드
        Estimate estimate = contractLoader.loadEstimateByContract(contract);
        Proposal proposal = contractLoader.loadProposalByContract(contract);
        Project project = contractLoader.loadProject(contract.getProject().getProjectId());
        Pipeline pipeline = project.getPipeline();

        // 6. 같은 프로젝트에 속한 estimate, proposal 처리
        List<Estimate> projectEstimates = contractLoader.loadEstimatesByProject(project);
        for (Estimate e : projectEstimates) {
            if (!e.getEstimateId().equals(estimate != null ? estimate.getEstimateId() : null)) {
                e.cancel();
            }
        }
        List<Proposal> projectProposals = contractLoader.loadProposalsByProject(project);
        for (Proposal p : projectProposals) {
            if (!p.getProposalId().equals(proposal != null ? proposal.getProposalId() : null)) {
                p.cancel();
            }
        }
        List<Contract> projectContracts = contractLoader.loadContractsByProject(project);
        for (Contract c : projectContracts) {
            if (!c.getContractId().equals(contract != null ? contract.getContractId() : null)) {
                c.cancel();
            }
        }

        // 7. 연관 estimate, proposal 상태 변경
        contract.complete();
        if (estimate != null) {
            estimate.complete();
        }
        if (proposal != null) {
            proposal.complete();
        }

        // 8. 매장-계약 매핑 조회 (STORE_CONTRACT_MAP 기준)
        List<StoreContractMap> storeContracts = storeContractMapRepository.findByContract(contract);

        // 9. Tenant 생성
        List<Store> stores = storeContracts.stream()
                .map(StoreContractMap::getStore)
                .collect(Collectors.toList());

        List<StoreTenantMap> tenantList = storeTenantMapMapper.toEntity(contract, stores);
        storeTenantMapRepository.saveAll(tenantList);

        // 10. Store 상태 변경
        for (StoreContractMap scm : storeContracts) {
            scm.getStore().occupy();
        }

        // 11. Revenue 생성
        Revenue revenue = revenueMapper.toEntity(contract, storeContracts, user);
        revenueRepository.save(revenue);

        // 12. 파이프라인 상태 변경
        if(pipeline != null) {
            pipeline.autoAdvance(
                    5,
                    Pipeline.StageName.CONTRACT_SUCCESS,
                    Pipeline.Status.COMPLETED
            );
        }

        // 13. Dto 생성
        return contractMapper.toCompleteResponseDto(contract, proposal, estimate, revenue, tenantList);
    }

    @Override
    public ContractDeleteResponseDto deleteContract(Long contractId, Long userId) {
        // 1. 엔티티 로딩
        User user = contractLoader.loadUser(userId);
        Contract contract = contractLoader.loadContract(contractId);

        // 2. 사용자 권한, 계약 상태 검증
        contractValidator.validateEditPermission(contract, user);
        contractValidator.validateCancelStatus(contract);

        // 3. 상태 변경
        contract.cancel();

        // 4. 반환
        return contractMapper.toCancelResponseDto(contract);
    }

    @Override
    @Transactional
    public UpdateContractResponseDto updateContract(Long contractId, UpdateContractRequestDto dto, Long userId) {
        // 1. 견적, 사용자 로딩
        Contract contract = contractLoader.loadContract(contractId);
        User user = contractLoader.loadUser(userId);

        // 2. 권한 체크
        contractValidator.validateEditPermission(contract, user);

        // 3. 상태 검증
        contractValidator.validateCompleteStatus(contract);

        // 4. 필수값 체크
        contractValidator.validateUpdate(dto, userId, contract);

        // 5. 연관 엔티티 로딩
        Project project = contractLoader.loadProject(dto.getProjectId());
        Pipeline pipeline = project.getPipeline();
        Client client = contractLoader.loadClient(dto.getClientId());
        ClientCompany clientCompany = contractLoader.loadCompany(dto.getClientCompanyId());

        Estimate estimate = null;
        if (dto.getEstimateId() != null) {
            estimate = contractValidator.validateEstimate(dto.getEstimateId());
        }

        // 6. DTO 공간 검증
        contractValidator.validateUpdateSpaces(dto);

        // 7. 기존 Contract에 연결된 매장-계약 맵 조회
        List<StoreContractMap> existingMaps = contractLoader.loadStoreMapsByContract(contract);

        // 8. DTO에 없는 매장은 삭제
        Set<Long> newStoreIds = dto.getSpaces().stream()
                .map(UpdateContractSpaceRequestDto::getStoreId)
                .collect(Collectors.toSet());

        List<StoreContractMap> toDeleteContractStoreMaps = existingMaps.stream()
                .filter(map -> !newStoreIds.contains(map.getStore().getStoreId()))
                .toList();

        storeContractMapRepository.deleteAll(toDeleteContractStoreMaps);
        contract.getStoreContractMaps().removeAll(toDeleteContractStoreMaps);
        existingMaps.removeAll(toDeleteContractStoreMaps);

        // 9. 수정/추가
        for (UpdateContractSpaceRequestDto spaceDto : dto.getSpaces()) {
            Optional<StoreContractMap> existingMapOpt = existingMaps.stream()
                    .filter(map -> map.getStore().getStoreId().equals(spaceDto.getStoreId()))
                    .findFirst();

            if (existingMapOpt.isPresent()) {
                // 기존 매장 업데이트
                StoreContractMap existingMap = existingMapOpt.get();

                long rentPrice = existingMap.getStore().getRentPrice();
                switch (dto.getContractType()) {
                    case LEASE:
                        break;
                    case CONSIGNMENT:
                        rentPrice = 0L;
                        break;
                    case MIX:
                        break;
                }
                existingMap.update(rentPrice, spaceDto.getAdditionalFee(), spaceDto.getDiscountAmount(), spaceDto.getDescription());
            } else {
                // 새로운 매장 추가
                Store store = contractLoader.loadStore(spaceDto.getStoreId());
                StoreContractMap newMap = storeContractMapMapper.toEntity(spaceDto, contract, store);
                storeContractMapRepository.save(newMap);
                contract.getStoreContractMaps().add(newMap);
                existingMaps.add(newMap);
            }
        }

        // 10. 총 금액 계산
        long totalAmount = existingMaps.stream()
                .mapToLong(StoreContractMap::getFinalContractAmount)
                .sum();

        // 11. 타입별 금액/수수료 강제 적용
        BigDecimal commissionRate = dto.getCommissionRate();
        long contractAmount = dto.getContractAmount() != null ? dto.getContractAmount() : 0L;

        switch(dto.getContractType()) {
            case LEASE:
                commissionRate = BigDecimal.ZERO;
                break;
            case CONSIGNMENT:
                contractAmount = 0;
                break;
            case MIX:
                // MIX는 그대로 사용
                break;
        }

        // 12. Contract 엔티티 업데이트
        contract.update(
                dto.getContractTitle(),
                dto.getCurrency(),
                contractAmount,
                commissionRate,
                dto.getContractType(),
                dto.getPaymentCondition(),
                dto.getContractStartDate(),
                dto.getContractEndDate(),
                dto.getContractDate(),
                dto.getRemark(),
                project,
                pipeline,
                client,
                clientCompany,
                estimate
        );
        contract.updateTotalAmount(totalAmount + (contract.getContractAmount() != null ? contract.getContractAmount() : 0));

        contractRepository.flush();

        // 13. 응답 DTO 반환
        return contractMapper.toUpdateResponseDto(contract);
    }
}
