package com.fiveguys.RIA.RIA_Backend.admin.model.exception;


import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;

public class AdminException extends CustomException {

  public AdminException(ErrorCode errorCode) {
    super(errorCode);
  }
}