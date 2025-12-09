package com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PopupDailySalesItem {

  private int day;                 // 일자 (1~31)
  private Long storeTenantMapId;   // 팝업 매장 매핑 ID
  private String storeName;        // store_display_name

  private BigDecimal totalAmount;  // 해당 매장, 해당 일 매출
  private long totalCount;         // 해당 매장, 해당 일 거래 건수
}