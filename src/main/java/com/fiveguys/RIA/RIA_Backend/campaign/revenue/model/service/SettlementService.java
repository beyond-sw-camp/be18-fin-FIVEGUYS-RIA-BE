package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service;

import java.time.LocalDate;

public interface SettlementService {

  // 상설(정규) 매장 월 정산
  void settleMonthlyForRegular(int year, int month);

  void settleLastMonthForRegular();

  // 팝업/전시 매장 일 정산
  void settleDailyForTemporary(LocalDate targetDate);

  void settleYesterdayForTemporary();
}