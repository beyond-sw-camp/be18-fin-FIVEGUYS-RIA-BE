package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCompanySummaryResponseDto {

  private Long clientCompanyId;
  private String companyName;

  // 입주명
  private String storeDisplayName;

  // 면적(m²)
  private Double areaSize;

  // 입점일 (STORE_TENANT_MAP.startDate)
  private LocalDate moveInDate;

  // 계약 기간 (CONTRACT)
  private LocalDate contractStartDate;
  private LocalDate contractEndDate;

  // 총 임대료 (CONTRACT.totalAmount 기준)
  private Long totalRentAmount;

  // 매출 수수료율
  private BigDecimal commissionRate;
}
