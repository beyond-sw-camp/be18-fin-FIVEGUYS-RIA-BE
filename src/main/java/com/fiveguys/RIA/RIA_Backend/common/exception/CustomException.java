package com.fiveguys.RIA.RIA_Backend.common.exception;

import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
