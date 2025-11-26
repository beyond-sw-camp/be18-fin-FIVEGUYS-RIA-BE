package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FloorErrorCode implements ErrorCode {

    // 요청/입력 관련
    INVALID_ZONE_ID(HttpStatus.BAD_REQUEST, "FLOOR_INVALID_ZONE_ID", "유효하지 않은 존 ID입니다."),

    // 조회 관련
    FLOOR_NOT_FOUND(HttpStatus.NOT_FOUND, "FLOOR_NOT_FOUND", "해당 존에 대한 층 정보가 존재하지 않습니다."),

    ZONE_NOT_FOUND(HttpStatus.NOT_FOUND, "FLOOR_ZONE_NOT_FOUND", "존 정보를 찾을 수 없습니다."),

    // 층 오류
    INVALID_FLOOR_ID(HttpStatus.BAD_REQUEST, "FLOOR_INVALID_FLOOR_ID","유효하지 않은 층 ID입니다."),
    // 내부 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FLOOR_INTERNAL_SERVER_ERROR", "층 정보 처리 중 내부 서버 오류가 발생했습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

    FloorErrorCode(HttpStatus status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }



}
