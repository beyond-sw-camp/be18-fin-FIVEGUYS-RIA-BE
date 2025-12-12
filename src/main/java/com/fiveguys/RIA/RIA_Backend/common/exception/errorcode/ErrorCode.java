package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
