package com.fiveguys.RIA.RIA_Backend.auth.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthException extends AuthenticationException {

    private final AuthErrorCode errorCode;

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 AuthenticationException에 메시지 전달
        this.errorCode = errorCode;
    }
}
