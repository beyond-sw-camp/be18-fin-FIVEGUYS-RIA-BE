package com.fiveguys.RIA.RIA_Backend.ai.model.exception;

public class AiException extends RuntimeException {
    private final AiErrorCode errorCode;

    public AiException(AiErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AiErrorCode getErrorCode() {
        return errorCode;
    }
}
