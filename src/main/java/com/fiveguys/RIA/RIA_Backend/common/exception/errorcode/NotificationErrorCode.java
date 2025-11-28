package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NotificationErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자 정보를 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION_NOT_FOUND", "알림 정보를 찾을 수 없습니다."),
    NOTIFICATION_ALREADY_READ(HttpStatus.CONFLICT, "NOTIFICATION_ALREADY_READ", "이미 읽은 알림입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "알림 목록을 조회할 권한이 없습니다."),
    NOTIFICATION_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "NOTIFICATION_ALREADY_DELETED", "이미 삭제된 알림입니다."),
    TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "TYPE_NOT_FOUND", "알림 유형을 찾을 수 없습니다."),
    INVALID_CONTEXT(HttpStatus.BAD_REQUEST, "INVALID_CONTEXT", "알림 생성에 필요한 컨텍스트가 올바르지 않습니다."),
    PROJECT_ID_REQUIRED(HttpStatus.BAD_REQUEST, "PROJECT_ID_REQUIRED", "프로젝트 알림 생성에 필요한 프로젝트 ID가 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    NotificationErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
