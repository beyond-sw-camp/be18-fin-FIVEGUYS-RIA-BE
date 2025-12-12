package com.fiveguys.RIA.RIA_Backend.auth.exception;

import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements ErrorCode {

    // 로그인
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "INVALID_LOGIN", "사번 또는 비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),

    // Access Token 관련
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_ACCESS_TOKEN", "액세스 토큰이 유효하지 않습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED", "액세스 토큰이 만료되었습니다."),
    INVALID_TOKEN_CATEGORY(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN_CATEGORY", "잘못된 토큰 유형입니다."),
    BLACKLISTED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "BLACKLISTED_ACCESS_TOKEN", "로그아웃된 액세스 토큰입니다."),

    // Refresh Token 관련
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_REFRESH_TOKEN", "리프레시 토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN_EXPIRED", "리프레시 토큰이 만료되었습니다."),

    // 인증 / 인가 실패
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다."),

    // 비밀번호 관련
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH", "기존 비밀번호가 일치하지 않습니다."),
    PASSWORD_DUPLICATE(HttpStatus.BAD_REQUEST, "PASSWORD_DUPLICATE", "새 비밀번호는 기존 비밀번호와 달라야 합니다."),
    PASSWORD_CHANGE_REQUIRED(HttpStatus.FORBIDDEN, "PASSWORD_CHANGE_REQUIRED", "비밀번호 변경이 필요합니다."),

    // 기타
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AuthErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
