package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RevenueDetailResponseDto {

  // ============================
  // 프로젝트 정보
  // ============================
  private Long projectId;
  private String projectTitle;
  private String projectType;
  private String salesManagerName;

  // ============================
  // 계약 정보
  // ============================
  private Long contractId;
  private String contractTitle;
  private String contractType;
  private String contractStartDate;
  private String contractEndDate;
  //private String contractDate;

  private String currency;
  private BigDecimal commissionRate;
  private BigDecimal depositAmount;                     // 보증금(contractAmount)
  private String paymentCondition;

  // ============================
  // 고객사 정보
  // ============================
  private String clientCompanyName;
  private String clientName;

  // ============================
  // 매장 정보 - 계약이 여러 매장을 점유할 수 있으므로 리스트
    // ============================
  private List<StoreInfo> stores;

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class StoreInfo {
    private Long storeTenantMapId;
    private String floorName;
    private String storeNumber;
    private String storeDisplayName;
    private BigDecimal finalContractAmount;
  }


  // Revenue 기본 정보
  private Long revenueId;
  private Long baseRentSnapshot;
/*  private String status;
  private String remark;*/

  // 누적 정산 정보
  private BigDecimal totalSalesAccumulated;
  private BigDecimal commissionAmountAccumulated;
  private BigDecimal finalRevenueAccumulated;

  // 최신 정산 정보
  private Integer latestSettlementYear;
  private Integer latestSettlementMonth;

  private BigDecimal latestTotalSalesAmount;
  private BigDecimal latestCommissionRate;
  private BigDecimal latestCommissionAmount;
  private BigDecimal latestFinalRevenue;
}