package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.SalesMonthlyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardMonthContextLoader {

  private final SalesMonthlyRepository salesMonthlyRepository;

  public TargetMonthContext load(Integer year, Integer month) {
    int targetYear;
    int targetMonth;

    if (year == null || month == null) {
      List<Object[]> ymList = salesMonthlyRepository.findAllYearMonthOrderByLatest();
      if (ymList.isEmpty()) {
        return TargetMonthContext.empty();
      }
      Object[] first = ymList.get(0);
      targetYear = ((Number) first[0]).intValue();
      targetMonth = ((Number) first[1]).intValue();
    } else {
      targetYear = year;
      targetMonth = month;
    }

    int ym = targetYear * 100 + targetMonth;

    boolean hasPrev = salesMonthlyRepository.existsBefore(ym);
    boolean hasNext = salesMonthlyRepository.existsAfter(ym);

    return TargetMonthContext.of(targetYear, targetMonth, ym, hasPrev, hasNext);
  }
}
