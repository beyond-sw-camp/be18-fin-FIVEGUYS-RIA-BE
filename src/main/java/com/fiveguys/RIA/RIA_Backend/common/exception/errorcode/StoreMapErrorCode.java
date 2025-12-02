package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StoreMapErrorCode implements ErrorCode{

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND,"Store Not Found","해당 하는 매장을 찾을 수 없습니다."),
    NO_AVAILABLE_SPACE(HttpStatus.NOT_FOUND, "AVAILABLE STORE NOT FOUND","사용 가능 한 매장이 존재 하지 않습니다."),
    INVALID_KEYWORD(HttpStatus.BAD_REQUEST, "INVALID KEYWORD", "유효하지 않은 검색어 입니다." ),
    SPACE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST,"SPACE NOT AVAILABLE","해당 공간을 이용 가능한 상태가 아닙니다."),
    INVALID_STORE_ID(HttpStatus.BAD_REQUEST, "INVALID STORE ID","유효하지 않은 매장 아이디 입니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

    StoreMapErrorCode(HttpStatus status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
