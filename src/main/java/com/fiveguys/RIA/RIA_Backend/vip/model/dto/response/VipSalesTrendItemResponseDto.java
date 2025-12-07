package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipSalesTrendItemResponseDto {

  private int year;
  private int month;
  private BigDecimal vipRatio; // 해당 월 VIP 매출 비중 (%)
}
