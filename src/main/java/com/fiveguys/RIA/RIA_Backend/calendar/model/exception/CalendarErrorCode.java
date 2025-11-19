package com.fiveguys.RIA.RIA_Backend.calendar.model.exception;

import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CalendarErrorCode implements ErrorCode {

    // ===== 공통 =====
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "잘못된 입력입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."),

    // ===== Google API 공통 오류 =====
    GOOGLE_API_BAD_REQUEST(HttpStatus.BAD_REQUEST, "GOOGLE_API_BAD_REQUEST", "Google API 요청 형식이 잘못되었습니다."),
    GOOGLE_API_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "GOOGLE_API_UNAUTHORIZED", "Google API 인증에 실패했습니다."),
    GOOGLE_API_FORBIDDEN(HttpStatus.FORBIDDEN, "GOOGLE_API_FORBIDDEN", "Google Calendar 접근 권한이 없습니다."),
    GOOGLE_API_RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "GOOGLE_API_RATE_LIMIT", "Google API 요청 한도를 초과했습니다."),
    GOOGLE_API_ERROR(HttpStatus.BAD_GATEWAY, "GOOGLE_API_ERROR", "Google Calendar API 호출 중 오류가 발생했습니다."),

    // ===== 메모 / 이벤트 =====
    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMO_NOT_FOUND", "해당 메모를 찾을 수 없습니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_DATE_FORMAT", "잘못된 날짜 형식입니다."),
    INVALID_COLOR_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_COLOR_FORMAT", "잘못된 색상 형식입니다."),

    // ===== 사용자 공유 (ACL) =====
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_EMAIL_FORMAT", "올바른 이메일 형식이 아닙니다."),
    USER_ALREADY_SHARED(HttpStatus.CONFLICT, "USER_ALREADY_SHARED", "이미 공유된 사용자입니다."),
    USER_NOT_SHARED(HttpStatus.NOT_FOUND, "USER_NOT_SHARED", "해당 사용자는 공유 설정에 없습니다."),

    // ===== 서비스 계정 인증 오류 =====
    SERVICE_ACCOUNT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "SERVICE_ACCOUNT_NOT_FOUND", "Google 서비스 계정 파일을 찾을 수 없습니다."),
    SERVICE_ACCOUNT_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SERVICE_ACCOUNT_LOAD_FAILED", "Google 서비스 계정 파일을 불러오는 중 오류가 발생했습니다."),
    GOOGLE_CREDENTIAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GOOGLE_CREDENTIAL_ERROR", "Google 인증 구성 과정에서 오류가 발생했습니다."),
    GOOGLE_TRANSPORT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GOOGLE_TRANSPORT_ERROR", "Google API 네트워크/트랜스포트 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    CalendarErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
    }
}

