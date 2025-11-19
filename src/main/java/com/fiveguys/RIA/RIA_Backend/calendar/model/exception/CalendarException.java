package com.fiveguys.RIA.RIA_Backend.calendar.model.exception;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;

public class CalendarException extends CustomException {

    public CalendarException(ErrorCode errorCode) {
        super(errorCode);
    }
}
