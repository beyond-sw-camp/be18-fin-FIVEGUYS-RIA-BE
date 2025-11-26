package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.EstimateErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EstimateValidator {

    // 생성 요청 전체 검증
    public void validateCreate(EstimateCreateRequestDto dto) {

        if (dto.getCreatedUserId() == null) {
            throw new CustomException(EstimateErrorCode.CREATED_USER_REQUIRED);
        }
        if (dto.getClientCompanyId() == null) {
            throw new CustomException(EstimateErrorCode.CLIENT_COMPANY_REQUIRED);
        }
        if (dto.getClientId() == null) {
            throw new CustomException(EstimateErrorCode.CLIENT_REQUIRED);
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new CustomException(EstimateErrorCode.TITLE_REQUIRED);
        }
        if (dto.getPaymentCondition() == null || dto.getPaymentCondition().isBlank()) {
            throw new CustomException(EstimateErrorCode.INVALID_PAYMENT_CONDITION);
        }

        // 날짜 검증
        validateDates(dto.getEstimateDate(), dto.getDeliveryDate());

        // spaces 검증
        validateSpaces(dto);
    }

    // 날짜 검증
    public void validateDates(LocalDate estimateDate, LocalDate deliveryDate) {
        if (estimateDate == null || deliveryDate == null) {
            throw new CustomException(EstimateErrorCode.INVALID_DATE_REQUIRED);
        }
        if (estimateDate.isAfter(deliveryDate)) {
            throw new CustomException(EstimateErrorCode.INVALID_DATE_RANGE);
        }
    }

    // 공간 리스트 검증
    private void validateSpaces(EstimateCreateRequestDto dto) {
        if (dto.getSpaces() == null || dto.getSpaces().isEmpty()) {
            throw new CustomException(EstimateErrorCode.SPACE_LIST_REQUIRED);
        }

        dto.getSpaces().forEach(space -> {
            if (space.getStoreId() == null) {
                throw new CustomException(EstimateErrorCode.STORE_REQUIRED);
            }
            if (space.getAdditionalFee() != null && space.getAdditionalFee() < 0) {
                throw new CustomException(EstimateErrorCode.INVALID_PRICE);
            }
            if (space.getDiscountAmount() != null && space.getDiscountAmount() < 0) {
                throw new CustomException(EstimateErrorCode.INVALID_PRICE);
            }
        });
    }

}
