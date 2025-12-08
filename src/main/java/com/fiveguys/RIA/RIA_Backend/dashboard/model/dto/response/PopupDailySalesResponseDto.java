// PopupDailySalesResponseDto.java
package com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PopupDailySalesResponseDto {

  private int year;
  private int month;

  private boolean hasPrevMonth;
  private boolean hasNextMonth;

  // 해당 월 일별 매출 (POPUP/EXHIBITION 전부 합산)
  private List<PopupDailySalesItem> days;
}
