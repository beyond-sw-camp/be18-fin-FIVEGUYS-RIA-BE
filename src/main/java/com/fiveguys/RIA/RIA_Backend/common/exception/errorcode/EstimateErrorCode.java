package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EstimateErrorCode implements ErrorCode {

    // 요청/입력 관련
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "ESTIMATE_INVALID_REQUEST", "요청 데이터가 올바르지 않습니다."),
    TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "ESTIMATE_TITLE_REQUIRED", "견적 제목은 필수 항목입니다."),
    CLIENT_COMPANY_REQUIRED(HttpStatus.BAD_REQUEST, "ESTIMATE_CLIENT_COMPANY_REQUIRED", "고객사는 필수 항목입니다."),
    CLIENT_REQUIRED(HttpStatus.BAD_REQUEST, "ESTIMATE_CLIENT_REQUIRED", "고객 담당자는 필수 항목입니다."),
    STORE_REQUIRED(HttpStatus.BAD_REQUEST, "ESTIMATE_STORE_REQUIRED", "공간(매장)은 필수 항목입니다."),
    CREATED_USER_REQUIRED(HttpStatus.BAD_REQUEST, "ESTIMATE_CREATED_USER_REQUIRED", "작성자는 필수 항목입니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "ESTIMATE_INVALID_PRICE", "금액은 0 이상이어야 합니다."),
    INVALID_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "ESTIMATE_INVALID_DATE_REQUIRED", "견적일과 납기일은 필수입니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "ESTIMATE_INVALID_DATE_RANGE", "견적일은 납기일보다 이후일 수 없습니다."),
    INVALID_PAYMENT_CONDITION(HttpStatus.BAD_REQUEST, "ESTIMATE_INVALID_PAYMENT_CONDITION", "유효하지 않은 결제 조건입니다."),
    SPACE_LIST_REQUIRED(HttpStatus.BAD_REQUEST,"SPACE_LIST_REQUIRED","견적에 공간 정보가 필수입니다."),
    // 조회 관련
    ESTIMATE_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_NOT_FOUND", "해당 견적서를 찾을 수 없습니다."),
    DUPLICATE_TITLE(HttpStatus.BAD_REQUEST, "ESTIMATE_DUPLICATE_TITLE", "이미 존재하는 견적 제목입니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_PROJECT_NOT_FOUND", "존재하지 않는 프로젝트입니다."),
    PIPELINE_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_PIPELINE_NOT_FOUND", "존재하지 않는 파이프라인입니다."),
    CLIENT_COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_CLIENT_COMPANY_NOT_FOUND", "존재하지 않는 고객사입니다."),
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_CLIENT_NOT_FOUND", "존재하지 않는 고객 담당자입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_STORE_NOT_FOUND", "존재하지 않는 매장입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
    PROPOSAL_NOT_FOUND(HttpStatus.NOT_FOUND, "PROPOSAL_NOT_FOUND","존재하지 않는 제안서 입니다."),
    // 내부 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ESTIMATE_INTERNAL_SERVER_ERROR", "견적 처리 중 서버 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ESTIMATE_DATABASE_ERROR", "견적 DB 처리 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    EstimateErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
