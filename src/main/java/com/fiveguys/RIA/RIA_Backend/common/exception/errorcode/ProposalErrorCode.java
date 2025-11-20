package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProposalErrorCode implements ErrorCode {


  // 등록 / 생성 관련

  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "PROPOSAL_INVALID_REQUEST", "요청 데이터가 올바르지 않습니다."),
  DUPLICATE_PROPOSAL(HttpStatus.CONFLICT, "PROPOSAL_DUPLICATE", "동일한 제목의 제안서가 이미 존재합니다."),
  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROPOSAL_PROJECT_NOT_FOUND", "존재하지 않는 프로젝트입니다."),
  PIPELINE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROPOSAL_PIPELINE_NOT_FOUND", "존재하지 않는 파이프라인입니다."),
  CLIENT_COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "PROPOSAL_CLIENT_COMPANY_NOT_FOUND", "존재하지 않는 고객사입니다."),
  CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROPOSAL_CLIENT_NOT_FOUND", "존재하지 않는 고객 담당자입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "PROPOSAL_USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
  CLIENT_COMPANY_MISMATCH(HttpStatus.BAD_REQUEST, "PROPOSAL_CLIENT_COMPANY_MISMATCH", "해당 고객은 지정한 고객사에 속하지 않습니다."),
  PROPOSAL_NOT_FOUND(HttpStatus.NOT_FOUND, "PROPOSAL_NOT_FOUND", "해당 제안서를 찾을 수 없습니다."),
  CANNOT_MODIFY_CANCELED_PROPOSAL(HttpStatus.FORBIDDEN, "CANNOT_MODIFY_CANCELED_PROPOSAL", "취소된 제안서는 수정할 수 없습니다."),
  TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "PROPOSAL_TITLE_REQUIRED", "제안서 제목은 필수 항목입니다."),
  SUBMIT_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "PROPOSAL_SUBMIT_DATE_REQUIRED", "제안서 제출일은 필수 항목입니다."),
  CREATED_USER_NOT_FOUND(HttpStatus.BAD_REQUEST,"CREATED_USER_NOT_FOUND" ,"유저를 찾을 수 없습니다" ),
  // 상태 / 접근 관련
  INVALID_STATUS(HttpStatus.BAD_REQUEST, "PROPOSAL_INVALID_STATUS", "유효하지 않은 제안서 상태입니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "PROPOSAL_ACCESS_DENIED", "제안서에 대한 접근 권한이 없습니다."),

  // 서버 및 내부 처리
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PROPOSAL_INTERNAL_SERVER_ERROR", "제안서 처리 중 서버 내부 오류가 발생했습니다."),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PROPOSAL_DATABASE_ERROR", "데이터베이스 처리 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ProposalErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
