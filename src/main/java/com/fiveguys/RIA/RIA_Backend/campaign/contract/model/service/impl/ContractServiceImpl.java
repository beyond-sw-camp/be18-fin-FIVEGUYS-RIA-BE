package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component.ContractLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component.ContractMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component.ContractValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component.StoreContractMapMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.ContractSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractCompleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.CreateContractResponseDto;
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
import com.fiveguys.RIA.RIA_Backend.campaign.tenant.model.component.StoreTenantMapMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.tenant.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.campaign.tenant.model.repository.StoreTenantMapRepository;
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

import java.time.LocalDate;
import java.util.List;
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
        Pipeline pipeline = contractLoader.loadPipeline(dto.getPipelineId());
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

        for (ContractSpaceRequestDto spaceDto : dto.getSpaces()) {
            // 매장 로딩
            Store store = contractLoader.loadStore(spaceDto.getStoreId());

            // Mapper에서 DTO + Contract + Store + 견적 참조로 엔티티 변환
            StoreContractMap map = storeContractMapMapper.toEntity(spaceDto, contract, store);

            storeContractMapRepository.save(map);

            totalAmount += map.getFinalContractAmount();
        }

        // 보증금 + 계약금액 = 총 계약 금액
        contract.updateTotalAmount(totalAmount + (contract.getContractAmount() != null ? contract.getContractAmount() : 0));

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
        contractValidator.validateCompletePermission(contract, user);

        // 유효값 검증이 필요할듯. 계약 시작일이 기존 계약 마감일보다 빠르면 안되는 등
        // 계약 타입에서 선불이면 매출 올리고 시작해야 할듯?
        // 계약이 완료되고, estimate, proposal 상태 변화를 하면서 같은 프로젝트에 묶인
        // complete를 제외한 나머지 estimate, proposal은 전부 다른 상태값 처리

        // 3. 상태 검증
        contractValidator.validateCompleteStatus(contract);

        // 4. 연관 엔티티 로드
        Estimate estimate = contractLoader.loadEstimateByContract(contract);
        Proposal proposal = contractLoader.loadProposalByContract(contract);

        // 5. 상태 변경
        contract.complete();
        if (estimate != null) {
            estimate.complete();
        }
        if (proposal != null) {
            proposal.complete();
        }

        // 6. 매장-계약 매핑 조회 (STORE_CONTRACT_MAP 기준)
        List<StoreContractMap> storeContracts = storeContractMapRepository.findByContract(contract);

        // 7. Tenant 생성
        List<Store> stores = storeContracts.stream()
                .map(StoreContractMap::getStore)
                .collect(Collectors.toList());

        List<StoreTenantMap> tenantList = storeTenantMapMapper.toEntity(contract, stores);
        storeTenantMapRepository.saveAll(tenantList);

        // 8. Revenue 생성
        Revenue revenue = revenueMapper.toEntity(contract, storeContracts, user);
        revenueRepository.save(revenue);

        // 9. Dto 생성
        return contractMapper.toCompleteResponseDto(contract, proposal, estimate, revenue, tenantList);
    }

}
