package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component;

import com.fiveguys.RIA.RIA_Backend.auth.service.entity.Role;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.UpdateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.UpdateContractSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ContractErrorCode;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.EstimateErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreTenantMapRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ContractValidator {

    private final ContractLoader contractLoader;
    private final ContractRepository contractRepository;
    private final StoreTenantMapRepository storeTenantMapRepository;

    // 생성 검증
    public void validateCreate(CreateContractRequestDto dto, Long userId) {

        validateRequiredFields(dto, userId);
        validateDate(dto);
        validateCommissionRate(dto);
        validateSpaces(dto);

        // 제목 중복 검증
        ClientCompany company = contractLoader.loadCompany(dto.getClientCompanyId());
        validateDuplicateTitle(dto.getContractTitle(), company);
    }

    private void validateRequiredFields(CreateContractRequestDto dto, Long userId) {
        if (userId == null)
            throw new CustomException(ContractErrorCode.CREATED_USER_REQUIRED);
        if (dto.getClientCompanyId() == null)
            throw new CustomException(ContractErrorCode.CLIENT_COMPANY_REQUIRED);
        if (dto.getClientId() == null)
            throw new CustomException(ContractErrorCode.CLIENT_REQUIRED);
        if (dto.getContractTitle() == null || dto.getContractTitle().isBlank())
            throw new CustomException(ContractErrorCode.CONTRACT_TITLE_REQUIRED);
        if (dto.getContractType() == null)
            throw new CustomException(ContractErrorCode.CONTRACT_TYPE_REQUIRED);
        if (dto.getPaymentCondition() == null || dto.getPaymentCondition().isBlank())
            throw new CustomException(ContractErrorCode.PAYMENT_CONDITION_REQUIRED);
        if (dto.getProjectId() == null)
            throw new CustomException(ContractErrorCode.PROJECT_NOT_FOUND);
    }

    private void validateCommissionRate(CreateContractRequestDto dto) {
        BigDecimal rate = dto.getCommissionRate();
        if (rate == null) return;

        if (rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new CustomException(ContractErrorCode.COMMISSION_RATE_INVALID);
        }
    }

    private void validateDate(CreateContractRequestDto dto) {
        if (dto.getContractStartDate() == null || dto.getContractEndDate() == null)
            throw new CustomException(ContractErrorCode.CONTRACT_DATE_REQUIRED);

        if (dto.getContractEndDate().isBefore(dto.getContractStartDate()))
            throw new CustomException(ContractErrorCode.CONTRACT_DATE_INVALID);

    }

    private void validateSpaces(CreateContractRequestDto dto) {
        List<CreateContractSpaceRequestDto> spaces = dto.getSpaces();
        if (spaces == null || spaces.isEmpty()) {
            throw new CustomException(ContractErrorCode.SPACE_LIST_REQUIRED);
        }

        LocalDate newStart = dto.getContractStartDate();
        LocalDate newEnd = dto.getContractEndDate();

        Set<Long> seenStoreIds = new HashSet<>();

        for (CreateContractSpaceRequestDto space : spaces) {

            if (space.getStoreId() == null) {
                throw new CustomException(ContractErrorCode.STORE_REQUIRED);
            }

            // 중복 매장 체크
            if (!seenStoreIds.add(space.getStoreId())) {
                throw new CustomException(ContractErrorCode.DUPLICATE_STORE);
            }

            // Store 로드
            Store store = contractLoader.loadStore(space.getStoreId());
            if (store == null) {
                throw new CustomException(ContractErrorCode.STORE_NOT_FOUND);
            }

            // 금액 항목 검증
            if (space.getAdditionalFee() != null && space.getAdditionalFee() < 0) {
                throw new CustomException(ContractErrorCode.INVALID_PRICE);
            }
            if (space.getDiscountAmount() != null && space.getDiscountAmount() < 0) {
                throw new CustomException(ContractErrorCode.INVALID_PRICE);
            }

            // rentPrice가 null 또는 음수인 경우
            Long rentPrice = store.getRentPrice();
            if (rentPrice == null || rentPrice < 0) {
                throw new CustomException(ContractErrorCode.RENT_PRICE_INVALID);
            }

            // Store 상태 검사
            switch (store.getStatus()) {
                case AVAILABLE:
                    break;

                case RESERVED:
                case MAINTENANCE:
                    throw new CustomException(ContractErrorCode.STORE_NOT_AVAILABLE);

                case OCCUPIED:
                    // OCCUPIED일 경우 StoreTenantMap을 확인해서 기간 겹침 여부 검사
                    validateOccupiedStore(store, newStart, newEnd);
                    break;

                default:
                    throw new CustomException(ContractErrorCode.STORE_NOT_AVAILABLE);
            }
        }
    }

    // 생성 검증

    public Estimate validateEstimate(Long estimateId) {
        if (estimateId == null) {
            throw new CustomException(ContractErrorCode.ESTIMATE_NOT_FOUND);
        }

        Estimate estimate = contractLoader.loadEstimate(estimateId);

        // isDeleted?
        if (estimate.getStatus() == Estimate.Status.CANCELED) {
            throw new CustomException(ContractErrorCode.ESTIMATE_NOT_FOUND);
        }

        return estimate;
    }

    public void validateEstimateAccess(Estimate estimate, User user) {

        boolean isOwner = estimate.getCreatedUser().getId().equals(user.getId());

        Role.RoleName role = user.getRole().getRoleName();
        boolean isLeaderOrAdmin =
                role == Role.RoleName.ROLE_ADMIN || role == Role.RoleName.ROLE_SALES_LEAD;

        // 조회 가능 조건: 관리자 / 팀장 / 작성자 본인
        if (!isOwner && !isLeaderOrAdmin) {
            throw new CustomException(ContractErrorCode.FORBIDDEN);
        }
    }

    public Long getAccessibleUserId(User user) {
        Role.RoleName role = user.getRole().getRoleName();
        boolean isLeaderOrAdmin = role == Role.RoleName.ROLE_ADMIN ||
                role == Role.RoleName.ROLE_SALES_LEAD;

        return isLeaderOrAdmin ? null : user.getId();
    }

    public void validateEditPermission(Contract contract, User user) {
        boolean isOwner = contract.getCreatedUser() != null &&
                contract.getCreatedUser().getId().equals(user.getId());

        Role.RoleName role = user.getRole() != null ? user.getRole().getRoleName() : null;
        boolean isLeaderOrAdmin = role == Role.RoleName.ROLE_ADMIN ||
                role == Role.RoleName.ROLE_SALES_LEAD;

        // 권한 조건: 작성자 or 관리자 or 팀장
        if (!isOwner && !isLeaderOrAdmin) {
            throw new CustomException(ContractErrorCode.FORBIDDEN);
        }
    }

    public void validateCompleteStatus(Contract contract) {
        // 1. 이미 완료된 계약이면 불가
        if (contract.getStatus() == Contract.Status.COMPLETED) {
            throw new CustomException(ContractErrorCode.ALREADY_COMPLETED);
        }

        // 2. 취소 또는 삭제된 계약이면 불가
        if (contract.getStatus() == Contract.Status.CANCELED /*||
                contract.isDeleted()*/) {
            throw new CustomException(ContractErrorCode.INVALID_STATUS);
        }

        // 3. 완료 가능한 상태인지 확인
        if (contract.getStatus() != Contract.Status.SUBMITTED) {
            throw new CustomException(ContractErrorCode.CANNOT_COMPLETE_FROM_STATUS);
        }

        // 4. 금액 검증
        if (contract.getContractAmount() == null || contract.getContractAmount() < 0) {
            throw new CustomException(ContractErrorCode.INVALID_CONTRACT_AMOUNT);
        }
    }

    public void validateCancelStatus(Contract contract) {

        // 이미 취소됨
        if (contract.getStatus() == Contract.Status.CANCELED) {
            throw new CustomException(ContractErrorCode.ALREADY_CANCELED);
        }

        // 이미 완료됨 → 취소 불가
        if (contract.getStatus() == Contract.Status.COMPLETED) {
            throw new CustomException(ContractErrorCode.CANNOT_DELETE_COMPLETE);
        }
    }

    // 수정 검증
    public void validateUpdateSpaces(UpdateContractRequestDto dto) {
        List<UpdateContractSpaceRequestDto> spaces = dto.getSpaces();
        LocalDate contractStartDate = dto.getContractStartDate();
        LocalDate contractEndDate = dto.getContractEndDate();

        if (spaces == null || spaces.isEmpty()) {
            throw new CustomException(ContractErrorCode.SPACE_LIST_REQUIRED);
        }

        Set<Long> seenStoreIds = new HashSet<>();

        for (UpdateContractSpaceRequestDto space : spaces) {
            // 필수값 검증
            if (space.getStoreId() == null) {
                throw new CustomException(ContractErrorCode.STORE_REQUIRED);
            }

            // 중복 매장 체크
            if (!seenStoreIds.add(space.getStoreId())) {
                throw new CustomException(ContractErrorCode.DUPLICATE_STORE);
            }

            // Store 로드
            Store store = contractLoader.loadStore(space.getStoreId());
            if (store == null) {
                throw new CustomException(ContractErrorCode.STORE_NOT_FOUND);
            }

            if (space.getAdditionalFee() == null || space.getAdditionalFee() < 0 ||
                    space.getDiscountAmount() == null || space.getDiscountAmount() < 0) {
                throw new CustomException(ContractErrorCode.INVALID_PRICE);
            }

            Long rentPrice = store.getRentPrice();
            if (rentPrice == null || rentPrice < 0) {
                throw new CustomException(ContractErrorCode.RENT_PRICE_INVALID);
            }

            // Store 상태 검사
            switch (store.getStatus()) {
                case AVAILABLE:
                    // 사용 가능, 통과
                    break;

                case RESERVED:
                case MAINTENANCE:
                    throw new CustomException(ContractErrorCode.STORE_NOT_AVAILABLE);

                case OCCUPIED:
                    // OCCUPIED일 경우 StoreTenantMap을 확인해서 기간 겹침 여부 검사
                    validateOccupiedStore(store, contractStartDate, contractEndDate);
                    break;

                default:
                    throw new CustomException(ContractErrorCode.STORE_NOT_AVAILABLE);
            }
        }
    }
    
    public void validateUpdate(UpdateContractRequestDto dto, Long userId, Contract contract) {
        validateRequiredFields(dto, userId);
        validateDate(dto);
        validateCommissionRate(dto);

        if (!contract.getContractTitle().equals(dto.getContractTitle())) {
            ClientCompany company = contract.getClientCompany();
            validateDuplicateTitle(dto.getContractTitle(), company);
        }
    }

    private void validateRequiredFields(UpdateContractRequestDto dto, Long userId) {
        if (userId == null)
            throw new CustomException(ContractErrorCode.CREATED_USER_REQUIRED);
        if (dto.getClientCompanyId() == null)
            throw new CustomException(ContractErrorCode.CLIENT_COMPANY_REQUIRED);
        if (dto.getClientId() == null)
            throw new CustomException(ContractErrorCode.CLIENT_REQUIRED);
        if (dto.getContractTitle() == null || dto.getContractTitle().isBlank())
            throw new CustomException(ContractErrorCode.CONTRACT_TITLE_REQUIRED);
        if (dto.getContractType() == null)
            throw new CustomException(ContractErrorCode.CONTRACT_TYPE_REQUIRED);
        if (dto.getPaymentCondition() == null || dto.getPaymentCondition().isBlank())
            throw new CustomException(ContractErrorCode.PAYMENT_CONDITION_REQUIRED);
        if (dto.getProjectId() == null)
            throw new CustomException(ContractErrorCode.PROJECT_REQUIRED);
    }

    private void validateCommissionRate(UpdateContractRequestDto dto) {
        BigDecimal rate = dto.getCommissionRate();
        if (rate == null) return;

        if (rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new CustomException(ContractErrorCode.COMMISSION_RATE_INVALID);
        }
    }

    private void validateDate(UpdateContractRequestDto dto) {
        if (dto.getContractStartDate() == null || dto.getContractEndDate() == null)
            throw new CustomException(ContractErrorCode.CONTRACT_DATE_REQUIRED);

        if (dto.getContractEndDate().isBefore(dto.getContractStartDate()))
            throw new CustomException(ContractErrorCode.CONTRACT_DATE_INVALID);
    }
    // 수정 검증

    public void validateStoresForCompletion(Contract contract) {
        List<StoreContractMap> storeContracts = contractLoader.loadStoreMapsByContract(contract);
        LocalDate startDate = contract.getContractStartDate();
        LocalDate endDate = contract.getContractEndDate();

        if (startDate == null || endDate == null) {
            throw new CustomException(ContractErrorCode.CONTRACT_DATE_REQUIRED);
        }
        if (endDate.isBefore(startDate)) {
            throw new CustomException(ContractErrorCode.CONTRACT_DATE_INVALID);
        }

        for (StoreContractMap scm : storeContracts) {
            Store store = scm.getStore();

            switch (store.getStatus()) {
                case AVAILABLE:
                    break;

                case RESERVED:
                case MAINTENANCE:
                    throw new CustomException(ContractErrorCode.STORE_NOT_AVAILABLE);

                case OCCUPIED:
                    validateOccupiedStore(store, startDate, endDate);
                    break;

                default:
                    throw new CustomException(ContractErrorCode.STORE_NOT_AVAILABLE);
            }
        }
    }

    private void validateOccupiedStore(Store store, LocalDate newStart, LocalDate newEnd) {
        List<StoreTenantMap> tenants = storeTenantMapRepository.findByStoreAndStatus(store, StoreTenantMap.Status.ACTIVE);

        for (StoreTenantMap tenant : tenants) {
            LocalDate existStart = tenant.getStartDate();
            LocalDate existEnd = tenant.getEndDate();

            // 무기한이면 무조건 충돌
            if (existEnd == null) {
                throw new CustomException(ContractErrorCode.STORE_OCCUPIED_DATE_CONFLICT);
            }

            boolean isOverlapping =
                    (newStart.isEqual(existEnd) || newStart.isBefore(existEnd))
                            && (existStart.isEqual(newEnd) || existStart.isBefore(newEnd));

            if (isOverlapping) {
                throw new CustomException(ContractErrorCode.STORE_OCCUPIED_DATE_CONFLICT);
            }
        }
    }

    // 제목 중복 여부
    private void validateDuplicateTitle(String title, ClientCompany company) {
        if (contractRepository.existsByContractTitleAndClientCompany(title, company)) {
            throw new CustomException(ContractErrorCode.DUPLICATE_TITLE);
        }
    }
}
