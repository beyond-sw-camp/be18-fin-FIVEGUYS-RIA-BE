package com.fiveguys.RIA.RIA_Backend.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RevenueErrorCode implements ErrorCode {

  INVALID_PERIOD(HttpStatus.BAD_REQUEST, "REVENUE_INVALID_PERIOD", "조회 기간이 올바르지 않습니다."),
  INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "REVENUE_INVALID_DATE_RANGE", "조회 일자가 올바르지 않습니다."),
  CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND, "REVENUE_CONTRACT_NOT_FOUND", "해당 계약을 찾을 수 없습니다."),
  STORE_TENANT_NOT_FOUND(HttpStatus.NOT_FOUND, "REVENUE_STORE_TENANT_NOT_FOUND", "해당 매장 매핑 정보를 찾을 수 없습니다."),
  SETTLEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "REVENUE_SETTLEMENT_NOT_FOUND", "정산 이력이 존재하지 않습니다."),
  DAILY_SALES_NOT_FOUND(HttpStatus.NOT_FOUND, "REVENUE_DAILY_SALES_NOT_FOUND", "일별 매출 데이터가 존재하지 않습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REVENUE_INTERNAL_SERVER_ERROR", "매출 처리 중 서버 오류가 발생했습니다."),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REVENUE_DATABASE_ERROR", "매출 데이터 처리 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  RevenueErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
