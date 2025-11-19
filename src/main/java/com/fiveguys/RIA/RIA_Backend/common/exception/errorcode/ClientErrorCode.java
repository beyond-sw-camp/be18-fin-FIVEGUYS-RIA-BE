package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ClientErrorCode implements ErrorCode {

  // 등록 / 요청 관련
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "CLIENT_INVALID_REQUEST", "요청 데이터가 올바르지 않습니다."),
  DUPLICATE_CLIENT(HttpStatus.CONFLICT, "CLIENT_DUPLICATE", "동일한 고객 담당자가 이미 존재합니다."),
  COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "CLIENT_COMPANY_NOT_FOUND", "존재하지 않는 고객사입니다."),
  CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CLIENT_NOT_FOUND", "존재하지 않는 고객 담당자입니다."),
  INVALID_CLIENT_TYPE(HttpStatus.BAD_REQUEST, "CLIENT_INVALID_TYPE", "유효하지 않은 담당자 유형입니다."),
  DUPLICATE_COMPANY(HttpStatus.CONFLICT, "COMPANY_DUPLICATE", "동일한 고객사가 이미 존재합니다."),
  EMPTY_CLIENT_NAME(HttpStatus.BAD_REQUEST, "CLIENT_EMPTY_NAME", "담당자 이름은 필수 입력값입니다."),
  EMPTY_CLIENT_PHONE(HttpStatus.BAD_REQUEST, "CLIENT_EMPTY_PHONE", "담당자 연락처는 필수 입력값입니다."),
  EMPTY_CLIENT_TYPE(HttpStatus.BAD_REQUEST, "CLIENT_EMPTY_TYPE", "담당자 유형은 필수 입력값입니다."),
  EMPTY_CLIENT_EMAIL(HttpStatus.BAD_REQUEST, "CLIENT_EMPTY_EMAIL", "담당자 이메일은 필수 입력값입니다."),

  // 신규 추가 (고객사 등록 검증)
  EMPTY_COMPANY_NAME(HttpStatus.BAD_REQUEST, "COMPANY_EMPTY_NAME", "고객사명을 입력해주세요."),
  EMPTY_CATEGORY(HttpStatus.BAD_REQUEST, "COMPANY_EMPTY_CATEGORY", "고객사 카테고리를 선택해주세요."),
  EMPTY_TYPE(HttpStatus.BAD_REQUEST, "COMPANY_EMPTY_TYPE", "고객사 유형을 선택해주세요."),
  DUPLICATE_BUSINESS_NUMBER(HttpStatus.CONFLICT, "COMPANY_DUPLICATE_BUSINESS_NUMBER", "동일한 사업자번호가 이미 존재합니다."),
  DUPLICATE_WEBSITE(HttpStatus.CONFLICT, "COMPANY_DUPLICATE_WEBSITE", "동일한 웹사이트 주소가 이미 존재합니다."),
  DUPLICATE_CLIENT_EMAIL(HttpStatus.CONFLICT, "CLIENT_DUPLICATE_EMAIL", "동일한 이메일이 이미 존재합니다."),

  // 삭제 / 접근 관련
  ALREADY_DELETED(HttpStatus.CONFLICT, "CLIENT_ALREADY_DELETED", "이미 삭제된 담당자입니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "CLIENT_ACCESS_DENIED", "해당 담당자에 대한 접근 권한이 없습니다."),

  // 서버 및 내부 처리
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CLIENT_INTERNAL_SERVER_ERROR", "담당자 처리 중 서버 내부 오류가 발생했습니다."),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CLIENT_DATABASE_ERROR", "데이터베이스 처리 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ClientErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}

