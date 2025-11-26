package com.fiveguys.RIA.RIA_Backend.admin.model.exception;

import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminErrorCode implements ErrorCode {

  // ===== 사용자 관리 관련 =====
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 사용자를 찾을 수 없습니다."),
  USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS", "이미 존재하는 사용자입니다."),
  INVALID_ROLE_ASSIGNMENT(HttpStatus.BAD_REQUEST, "INVALID_ROLE_ASSIGNMENT", "잘못된 권한이 지정되었습니다."),
  SELF_ROLE_MODIFICATION_FORBIDDEN(HttpStatus.FORBIDDEN, "SELF_ROLE_MODIFICATION_FORBIDDEN",
      "본인의 권한은 변경할 수 없습니다."),
  ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ROLE_NOT_FOUND", "해당 권한(Role) 정보를 찾을 수 없습니다."),

  // 같은 권한 변경 막기
  ROLE_ALREADY_ASSIGNED(HttpStatus.BAD_REQUEST, "ROLE_ALREADY_ASSIGNED", "이미 동일한 권한이 지정되어 있습니다."),

  // ===== 접근 제어 / 인증 관련 =====
  UNAUTHORIZED_ADMIN_ACCESS(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ADMIN_ACCESS",
      "관리자 접근 권한이 없습니다."),
  FORBIDDEN_OPERATION(HttpStatus.FORBIDDEN, "FORBIDDEN_OPERATION", "이 작업을 수행할 권한이 없습니다."),

  // ===== 시스템 / 서버 관련 =====
  REDIS_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS_CONNECTION_FAILED",
      "Redis 연결에 실패했습니다."),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", "데이터베이스 오류가 발생했습니다."),
  UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN_SERVER_ERROR",
      "알 수 없는 서버 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  AdminErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  // ErrorCode 인터페이스에서 요구하는 메서드 이름이 getStatus()라면
  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
