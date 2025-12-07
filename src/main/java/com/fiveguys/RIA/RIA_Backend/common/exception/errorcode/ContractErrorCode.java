package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ContractErrorCode implements ErrorCode {

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "CONTRACT_INVALID_REQUEST", "요청 데이터가 올바르지 않습니다."),
    CONTRACT_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_TITLE_REQUIRED", "계약 제목은 필수 항목입니다."),
    CLIENT_COMPANY_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_CLIENT_COMPANY_REQUIRED", "고객사는 필수 항목입니다."),
    CLIENT_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_CLIENT_REQUIRED", "고객 담당자는 필수 항목입니다."),
    SPACE_LIST_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_SPACE_LIST_REQUIRED", "계약에 공간 정보가 필수입니다."),
    CREATED_USER_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_CREATED_USER_REQUIRED", "작성자는 필수 항목입니다."),
    CONTRACT_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_TYPE_REQUIRED", "계약 유형은 필수 항목입니다."),
    PAYMENT_CONDITION_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_PAYMENT_CONDITION_REQUIRED", "결제 조건은 필수 항목입니다."),
    PAYMENT_CONDITION_MISMATCH(HttpStatus.BAD_REQUEST, "CONTRACT_PAYMENT_CONDITION_MISMATCH", "견적 결제 조건이 계약 결제 조건과 일치하지 않습니다."),
    PROJECT_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_PROJECT_REQUIRED", "프로젝트는 필수 항목입니다."),
    TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_TITLE_REQUIRED", "계약 제목은 필수 항목입니다."),
    CONTRACT_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_DATE_REQUIRED", "계약 시작일과 종료일은 필수입니다."),
    CONTRACT_DATE_INVALID(HttpStatus.BAD_REQUEST, "CONTRACT_DATE_INVALID", "계약 종료일은 시작일보다 빠를 수 없습니다."),
    STORE_REQUIRED(HttpStatus.BAD_REQUEST, "CONTRACT_STORE_REQUIRED", "공간(매장)은 필수 항목입니다."),
    RENT_PRICE_INVALID(HttpStatus.BAD_REQUEST, "CONTRACT_RENT_PRICE_INVALID", "임대료는 0 이상이어야 합니다."),
    DUPLICATE_STORE(HttpStatus.BAD_REQUEST, "CONTRACT_DUPLICATE_STORE", "동일한 매장이 중복 선택되었습니다."),
    COMMISSION_RATE_INVALID(HttpStatus.BAD_REQUEST, "CONTRACT_COMMISSION_RATE_INVALID", "수수료율은 0 이상이어야 합니다."),
    DUPLICATE_TITLE(HttpStatus.BAD_REQUEST, "CONTRACT_DUPLICATE_TITLE", "이미 존재하는 계약 제목입니다."),
    STORE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "STORE_NOT_AVAILABLE", "해당 매장은 현재 계약할 수 없는 상태입니다."),
    STORE_OCCUPIED_DATE_CONFLICT(HttpStatus.BAD_REQUEST, "STORE_OCCUPIED_DATE_CONFLICT", "매장 기존 입점 기간과 계약 기간이 겹칩니다."),
    LEASE_CANNOT_HAVE_COMMISSION(HttpStatus.BAD_REQUEST, "LEASE_CANNOT_HAVE_COMMISSION", "임대차 계약은 수수료율을 가질 수 없습니다."),
    LEASE_REQUIRES_RENT_AMOUNT(HttpStatus.BAD_REQUEST, "LEASE_REQUIRES_RENT_AMOUNT", "임대차 계약은 임대료가 필요합니다."),
    CONSIGNMENT_CANNOT_HAVE_RENT_AMOUNT(HttpStatus.BAD_REQUEST, "CONSIGNMENT_CANNOT_HAVE_RENT_AMOUNT", "위탁 계약은 임대료를 가질 수 없습니다."),
    CONSIGNMENT_REQUIRES_COMMISSION(HttpStatus.BAD_REQUEST, "CONSIGNMENT_REQUIRES_COMMISSION", "위탁 계약은 수수료율이 필요합니다."),
    MIX_REQUIRES_RENT_AMOUNT(HttpStatus.BAD_REQUEST, "MIX_REQUIRES_RENT_AMOUNT", "복합 계약은 임대료가 필요합니다."),
    MIX_REQUIRES_COMMISSION(HttpStatus.BAD_REQUEST, "MIX_REQUIRES_COMMISSION", "복합 계약은 수수료율이 필요합니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "CONTRACT_INVALID_PRICE", "유효하지 않은 금액입니다."),

    ESTIMATE_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT_ESTIMATE_NOT_FOUND", "해당 견적서를 찾을 수 없습니다."),
    CANCELED_ESTIMATE(HttpStatus.BAD_REQUEST, "CANCELED_ESTIMATE", "취소된 견적로는 계약을 생성할 수 없습니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT_PROJECT_NOT_FOUND", "존재하지 않는 프로젝트입니다."),
    PIPELINE_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT_PIPELINE_NOT_FOUND", "존재하지 않는 파이프라인입니다."),
    CLIENT_COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT_CLIENT_COMPANY_NOT_FOUND", "존재하지 않는 고객사입니다."),
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT_CLIENT_NOT_FOUND", "존재하지 않는 고객 담당자입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT_STORE_NOT_FOUND", "존재하지 않는 매장입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT_USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
    CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT_NOT_FOUND", "존재하지 않는 계약입니다."),

    PROJECT_MISMATCH(HttpStatus.BAD_REQUEST, "CONTRACT_PROJECT_MISMATCH", "견적 프로젝트와 계약 프로젝트가 일치하지 않습니다."),
    PIPELINE_MISMATCH(HttpStatus.BAD_REQUEST, "CONTRACT_PIPELINE_MISMATCH", "견적 파이프라인과 계약 파이프라인이 일치하지 않습니다."),
    CLIENT_COMPANY_MISMATCH(HttpStatus.BAD_REQUEST, "CONTRACT_CLIENT_COMPANY_MISMATCH", "견적 고객사와 계약 고객사가 일치하지 않습니다."),
    CLIENT_MISMATCH(HttpStatus.BAD_REQUEST, "CONTRACT_CLIENT_MISMATCH", "견적 고객 담당자와 계약 고객 담당자가 일치하지 않습니다."),

    DUPLICATE_CONTRACT(HttpStatus.BAD_REQUEST, "CONTRACT_DUPLICATE", "이미 유효한 계약이 존재하는 공간입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "CONTRACT_FORBIDDEN", "권한이 없는 사용자입니다."),
    TRANSACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CONTRACT_TRANSACTION_FAILED", "계약 처리 트랜잭션에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CONTRACT_INTERNAL_SERVER_ERROR", "계약 처리 중 서버 오류가 발생했습니다."),

    ALREADY_COMPLETED( HttpStatus.BAD_REQUEST, "CONTRACT_ALREADY_COMPLETED", "이미 완료된 계약입니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "CONTRACT_INVALID_STATUS", "해당 상태의 계약은 완료할 수 없습니다."),
    INVALID_EDIT_STATUS(HttpStatus.BAD_REQUEST, "INVALID_EDIT_STATUS", "해당 상태의 계약은 수정할 수 없습니다."),
    CANNOT_COMPLETE_FROM_STATUS(HttpStatus.BAD_REQUEST, "CONTRACT_CANNOT_COMPLETE_FROM_STATUS", "현재 계약 상태에서는 완료 처리를 할 수 없습니다."),
    INVALID_CONTRACT_AMOUNT(HttpStatus.BAD_REQUEST, "CONTRACT_INVALID_CONTRACT_AMOUNT", "계약 금액이 올바르지 않습니다."),
    ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "ALREADY_CANCELED" , "이미 삭제한 계약입니다."),
    CANNOT_DELETE_COMPLETE(HttpStatus.BAD_REQUEST, "CANNOT_DELETE_COMPLETE", "이미 완료된 계약은 취소할 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ContractErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
