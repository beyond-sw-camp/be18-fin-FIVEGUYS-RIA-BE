package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component;

import com.fiveguys.RIA.RIA_Backend.auth.service.entity.Role;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ContractErrorCode;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.EstimateErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractValidator {

    private final ContractLoader contractLoader;
    private final ContractRepository contractRepository;

    public void validateCreate(CreateContractRequestDto dto, Long userId) {
        // 1. 필수값 검증
        if (userId == null) {
            throw new CustomException(ContractErrorCode.CREATED_USER_REQUIRED);
        }
        if (dto.getClientCompanyId() == null) {
            throw new CustomException(ContractErrorCode.CLIENT_COMPANY_REQUIRED);
        }
        if (dto.getClientId() == null) {
            throw new CustomException(ContractErrorCode.CLIENT_REQUIRED);
        }
        if (dto.getContractTitle() == null || dto.getContractTitle().isBlank()) {
            throw new CustomException(ContractErrorCode.CONTRACT_TITLE_REQUIRED);
        }
        if (dto.getContractType() == null) {
            throw new CustomException(ContractErrorCode.CONTRACT_TYPE_REQUIRED);
        }
        if (dto.getPaymentCondition() == null || dto.getPaymentCondition().isBlank()) {
            throw new CustomException(ContractErrorCode.PAYMENT_CONDITION_REQUIRED);
        }
        if (dto.getProjectId() == null) {
            throw new CustomException(ContractErrorCode.PROJECT_NOT_FOUND);
        }

        // 공간 리스트 검증
        validateSpaces(dto);

        // 제목 중복 검증
        ClientCompany company = contractLoader.loadCompany(dto.getClientCompanyId());
        validateDuplicateTitle(dto.getContractTitle(), company);
    }

    // 공간 리스트 검증
    private void validateSpaces(CreateContractRequestDto dto) {
        if (dto.getSpaces() == null || dto.getSpaces().isEmpty()) {
            throw new CustomException(ContractErrorCode.SPACE_LIST_REQUIRED);
        }

        dto.getSpaces().forEach(space -> {
            // 1. 필수값 검증
            if (space.getStoreId() == null) {
                throw new CustomException(ContractErrorCode.STORE_REQUIRED);
            }

            // 2. 금액 검증
            if (space.getAdditionalFee() != null && space.getAdditionalFee() < 0) {
                throw new CustomException(EstimateErrorCode.INVALID_PRICE);
            }
            if (space.getDiscountAmount() != null && space.getDiscountAmount() < 0) {
                throw new CustomException(EstimateErrorCode.INVALID_PRICE);
            }

            // 3. 날짜 검증
            if (space.getContractStartDate() == null || space.getContractEndDate() == null) {
                throw new CustomException(ContractErrorCode.CONTRACT_DATE_REQUIRED);
            }
            if (space.getContractEndDate().isBefore(space.getContractStartDate())) {
                throw new CustomException(ContractErrorCode.CONTRACT_DATE_INVALID);
            }
        });
    }

    // 제목 중복 여부
    private void validateDuplicateTitle(String title, ClientCompany company) {
        if (contractRepository.existsByContractTitleAndClientCompany(title, company)) {
            throw new CustomException(ContractErrorCode.DUPLICATE_TITLE);
        }
    }

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

    public void validateCompletePermission(Contract contract, User user) {
        String roleName = String.valueOf(user.getRole().getRoleName());

        // 관리자
        if ("ROLE_ADMIN".equals(roleName))
            return;

        // 영업팀장
        if ("ROLE_SALES_LEAD".equals(roleName))
            return;

        // 3. 일반 영업 사원이라면 → 본인이 담당한 계약인지 확인
        Long creatorId = contract.getCreatedUser().getId();
        if (!creatorId.equals(user.getId())) {
            throw new CustomException(ContractErrorCode.FORBIDDEN);
        }
    }

    public void validateCompleteStatus(Contract contract) {
        // 1. 이미 완료된 계약이면 불가
        if (contract.getStatus() == Contract.Status.COMPLETED) {
            throw new CustomException(ContractErrorCode.ALREADY_COMPLETED);
        }

        // 2. 취소 또는 삭제된 계약이면 불가
        if (contract.getStatus() == Contract.Status.CANCELLED ||
                contract.isDeleted()) {
            throw new CustomException(ContractErrorCode.INVALID_STATUS);
        }

        // 3. 완료 가능한 상태인지 확인
        if (contract.getStatus() != Contract.Status.SUMMITTED) {
            throw new CustomException(ContractErrorCode.CANNOT_COMPLETE_FROM_STATUS);
        }

        // 4. 금액 검증
        if (contract.getContractAmount() == null || contract.getContractAmount() <= 0) {
            throw new CustomException(ContractErrorCode.INVALID_CONTRACT_AMOUNT);
        }
    }
}
