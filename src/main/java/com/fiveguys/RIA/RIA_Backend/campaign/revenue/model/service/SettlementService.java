package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service;

public interface SettlementService {

  /**
   * 주어진 연/월에 대해 월 정산 실행
   * - SALES_MONTHLY + 계약정보 → REVENUE_SETTLEMENT 생성/갱신
   * - REVENUE.TOTAL_PRICE 누적 재계산
   */
  void settleMonthly(int year, int month);

  /**
   * “지난달” 기준 자동 정산용 헬퍼
   */
  void settleLastMonth();
}