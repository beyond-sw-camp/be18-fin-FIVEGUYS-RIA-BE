package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PipelineErrorCode implements ErrorCode {

  // 404
  PIPELINE_NOT_FOUND(HttpStatus.NOT_FOUND, "PIPELINE_NOT_FOUND", "해당 파이프라인을 찾을 수 없습니다."),

  // 400
  INVALID_STAGE(HttpStatus.BAD_REQUEST, "INVALID_STAGE", "유효하지 않은 단계 번호입니다."),
  ALREADY_IN_STAGE(HttpStatus.BAD_REQUEST, "ALREADY_IN_STAGE", "이미 해당 단계에 위치해 있습니다."),
  INVALID_STAGE_FLOW(HttpStatus.BAD_REQUEST, "INVALID_STAGE_FLOW", "단계 변경 순서가 올바르지 않습니다."),

  // 403
  FORBIDDEN(HttpStatus.FORBIDDEN, "PIPELINE_FORBIDDEN", "파이프라인을 변경할 권한이 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  PipelineErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
