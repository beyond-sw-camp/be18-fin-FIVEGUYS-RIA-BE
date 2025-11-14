package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProjectErrorCode implements ErrorCode {

  // 등록 / 요청 관련
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "PROJECT_INVALID_REQUEST", "요청 데이터가 올바르지 않습니다."),
  DUPLICATE_PROJECT(HttpStatus.CONFLICT, "PROJECT_DUPLICATE", "동일한 이름의 프로젝트가 이미 존재합니다."),
  CLIENT_COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_CLIENT_COMPANY_NOT_FOUND", "존재하지 않는 고객사입니다."),
  CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_CLIENT_NOT_FOUND", "존재하지 않는 고객 담당자입니다."),
  SALES_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_SALES_MANAGER_NOT_FOUND", "존재하지 않는 영업 담당자입니다."),
  INVALID_PROJECT_TYPE(HttpStatus.BAD_REQUEST, "PROJECT_INVALID_TYPE", "유효하지 않은 프로젝트 유형입니다."),
  TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "PROJECT_TITLE_REQUIRED", "프로젝트 제목은 필수 항목입니다."),
  INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "PROJECT_INVALID_DATE_RANGE", "프로젝트 기간이 올바르지 않습니다."),
  INVALID_STATUS(HttpStatus.BAD_REQUEST, "PROJECT_INVALID_STATUS", "유효하지 않은 상태값입니다."),
  CANNOT_CANCEL_COMPLETED_PROJECT(HttpStatus.BAD_REQUEST, "CANNOT_CANCEL_COMPLETED_PROJECT","완료된 프로젝트는 취소할 수 없습니다."),
  TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "PROJECT_TYPE_REQUIRED", "프로젝트 유형은 필수 항목입니다."),

  // 조회 / 접근 관련
  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_NOT_FOUND", "존재하지 않는 프로젝트입니다."),
  NO_ACTIVE_PROJECTS(HttpStatus.NOT_FOUND, "PROJECT_NO_ACTIVE_PROJECTS", "진행 중인 프로젝트가 없습니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "PROJECT_ACCESS_DENIED", "해당 프로젝트에 대한 접근 권한이 없습니다."),
  ALREADY_COMPLETED(HttpStatus.CONFLICT, "PROJECT_ALREADY_COMPLETED", "이미 완료된 프로젝트입니다."),
  ALREADY_DELETED(HttpStatus.CONFLICT, "PROJECT_ALREADY_DELETED", "이미 삭제된 프로젝트입니다."),
  FORBIDDEN_UPDATE(HttpStatus.FORBIDDEN,"UPDATE_ACCESS_DENIED" ,"해당 프로젝트에 대한 수정 권한이 없습니다"),

  // 파이프라인 / 내부 로직 관련
  PIPELINE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROJECT_PIPELINE_CREATION_FAILED", "프로젝트 파이프라인 생성 중 오류가 발생했습니다."),
  PIPELINE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_PIPELINE_NOT_FOUND", "해당 프로젝트의 파이프라인을 찾을 수 없습니다."),

  // 서버 및 내부 처리
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PROJECT_INTERNAL_SERVER_ERROR", "프로젝트 처리 중 서버 내부 오류가 발생했습니다."),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PROJECT_DATABASE_ERROR", "데이터베이스 처리 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ProjectErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
