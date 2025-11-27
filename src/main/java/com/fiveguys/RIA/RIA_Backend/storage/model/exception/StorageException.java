package com.fiveguys.RIA.RIA_Backend.storage.model.exception;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;

public class StorageException extends CustomException {

    public StorageException(StorageErrorCode errorCode) {
        super(errorCode);
    }
}
