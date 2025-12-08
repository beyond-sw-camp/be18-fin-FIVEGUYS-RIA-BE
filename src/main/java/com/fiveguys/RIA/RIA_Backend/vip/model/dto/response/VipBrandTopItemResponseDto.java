package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipBrandTopItemResponseDto {

  private int rank;
  private String name;        // 브랜드/매장명
  private BigDecimal rate;    // VIP 매출 비중 (%)
}
