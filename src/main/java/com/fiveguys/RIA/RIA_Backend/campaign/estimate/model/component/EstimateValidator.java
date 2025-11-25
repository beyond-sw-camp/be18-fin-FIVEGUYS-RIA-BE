package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.EstimateErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EstimateValidator {

    // 생성 시 기본 필수값 체크
    public void validateCreate(EstimateCreateRequestDto dto) {
        if (dto.getCreatedUserId() == null) {
            // 담당자 필수 autofill로 바꾸면 필요없을듯
            throw new CustomException(EstimateErrorCode.CREATED_USER_REQUIRED);
        }
        if (dto.getClientCompanyId() == null) {
            throw new CustomException(EstimateErrorCode.CLIENT_COMPANY_REQUIRED);
        }
        if (dto.getClientId() == null) {
            throw new CustomException(EstimateErrorCode.CLIENT_REQUIRED);
        }
        if (dto.getStoreId() == null) {
            throw new CustomException(EstimateErrorCode.STORE_REQUIRED);
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new CustomException(EstimateErrorCode.TITLE_REQUIRED);
        }
        if (dto.getPaymentCondition() == null || dto.getPaymentCondition().isBlank()) {
            throw new CustomException(EstimateErrorCode.INVALID_PAYMENT_CONDITION);
        }
    }

    // 가격 검증
    public void validatePrices(Long base, Long add, Long discount) {
        if (base == null || add == null || discount == null) {
            throw new CustomException(EstimateErrorCode.INVALID_PRICE);
        }
        if (base < 0 || add < 0 || discount < 0) {
            throw new CustomException(EstimateErrorCode.INVALID_PRICE);
        }
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
}
