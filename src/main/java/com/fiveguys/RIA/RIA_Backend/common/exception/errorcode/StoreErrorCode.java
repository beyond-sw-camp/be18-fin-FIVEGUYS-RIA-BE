package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StoreErrorCode implements ErrorCode{

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND,"Store Not Found","해당 하는 매장을 찾을 수 없습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

    StoreErrorCode(HttpStatus status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
