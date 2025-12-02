package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "REVENUE_SETTLEMENT"
    /*
    ,indexes = {
        // 계약별 정산 조회 (REGULAR, POPUP 둘 다 공통)
        @Index(name = "IX_REVENUE_SETTLEMENT_CONTRACT", columnList = "CONTRACT_ID"),

        // 매장·브랜드·계약 매핑 + 정산 액세스 최적화
        @Index(name = "IX_REVENUE_SETTLEMENT_TENANT", columnList = "STORE_TENANT_MAP_ID"),

        // 프로젝트 매출 합산(프로젝트 → 계약 → 정산값 합계)
        @Index(name = "IX_REVENUE_SETTLEMENT_PROJECT", columnList = "PROJECT_ID")
    }
    */
)
public class RevenueSettlement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "REVENUE_SETTLEMENT_ID")
  private Long revenueSettlementId;

  @Column(name = "STORE_TENANT_MAP_ID", nullable = false)
  private Long storeTenantMapId;

  @Column(name = "CONTRACT_ID", nullable = false)
  private Long contractId;

  @Column(name = "PROJECT_ID", nullable = false)
  private Long projectId;

  @Column(name = "TOTAL_SALES_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal totalSalesAmount = BigDecimal.ZERO;

  @Column(name = "COMMISSION_RATE", nullable = false, precision = 5, scale = 2)
  private BigDecimal commissionRate = BigDecimal.ZERO;

  @Column(name = "COMMISSION_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal commissionAmount = BigDecimal.ZERO;

  @Column(name = "FINAL_REVENUE", nullable = false, precision = 15, scale = 2)
  private BigDecimal finalRevenue = BigDecimal.ZERO;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT", nullable = false)
  private LocalDateTime updatedAt;

  protected RevenueSettlement() {}

  public RevenueSettlement(Long storeTenantMapId,
      Long contractId,
      Long projectId) {
    this.storeTenantMapId = storeTenantMapId;
    this.contractId = contractId;
    this.projectId = projectId;
    this.totalSalesAmount = BigDecimal.ZERO;
    this.commissionAmount = BigDecimal.ZERO;
    this.finalRevenue = BigDecimal.ZERO;
    this.commissionRate = BigDecimal.ZERO;
  }

  // 금액 설정
  public void applySettlement(BigDecimal totalSales,
      BigDecimal commissionRate,
      BigDecimal commissionAmount,
      BigDecimal finalRevenue) {
    this.totalSalesAmount = totalSales;
    this.commissionRate = commissionRate;
    this.commissionAmount = commissionAmount;
    this.finalRevenue = finalRevenue;
  }

  @PrePersist
  void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public Long getRevenueSettlementId() { return revenueSettlementId; }
  public Long getStoreTenantMapId() { return storeTenantMapId; }
  public Long getContractId() { return contractId; }
  public Long getProjectId() { return projectId; }
  public BigDecimal getTotalSalesAmount() { return totalSalesAmount; }
  public BigDecimal getCommissionRate() { return commissionRate; }
  public BigDecimal getCommissionAmount() { return commissionAmount; }
  public BigDecimal getFinalRevenue() { return finalRevenue; }
}
