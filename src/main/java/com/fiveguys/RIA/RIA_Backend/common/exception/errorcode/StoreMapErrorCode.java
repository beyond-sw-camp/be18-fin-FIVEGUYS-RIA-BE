package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StoreMapErrorCode implements ErrorCode {

    // 404
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_NOT_FOUND", "매장을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    StoreMapErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
