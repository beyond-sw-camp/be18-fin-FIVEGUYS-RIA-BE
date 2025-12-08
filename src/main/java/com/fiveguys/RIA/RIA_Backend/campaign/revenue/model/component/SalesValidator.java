// SalesValidator.java
package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.RevenueErrorCode;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SalesValidator {

  // 연/월 기간 검증
  public void validateYearMonthRange(
      int startYear,
      int startMonth,
      int endYear,
      int endMonth
  ) {
    if (startYear <= 0 || startMonth <= 0 || startMonth > 12 ||
        endYear <= 0 || endMonth <= 0 || endMonth > 12) {
      throw new CustomException(RevenueErrorCode.INVALID_PERIOD);
    }

    int startYm = startYear * 100 + startMonth;
    int endYm = endYear * 100 + endMonth;

    if (startYm > endYm) {
      throw new CustomException(RevenueErrorCode.INVALID_PERIOD);
    }
  }

  // 일자 범위 검증
  public void validateDateRange(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      throw new CustomException(RevenueErrorCode.INVALID_DATE_RANGE);
    }
    if (startDate.isAfter(endDate)) {
      throw new CustomException(RevenueErrorCode.INVALID_DATE_RANGE);
    }
  }

  // 정산 이력 결과 검증
  public void validateSettlementResult(List<RevenueSettlement> settlements) {
    if (settlements == null || settlements.isEmpty()) {
      throw new CustomException(RevenueErrorCode.SETTLEMENT_NOT_FOUND);
    }
  }

  // 일별 매출 결과 검증
  public void validateDailySalesResult(List<SalesDaily> dailyList) {
    if (dailyList == null || dailyList.isEmpty()) {
      throw new CustomException(RevenueErrorCode.DAILY_SALES_NOT_FOUND);
    }
  }
}
