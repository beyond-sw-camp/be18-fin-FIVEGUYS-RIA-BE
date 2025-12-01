package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "SALES_YEARLY"
    /*
    ,indexes = {
        // 테넌트(매장+브랜드) 연간 매출 조회 / 랭킹용
        @Index(name = "IX_SALES_YEARLY_TENANT_YEAR", columnList = "STORE_TENANT_MAP_ID, SALES_YEAR"),
        // 특정 연도 전체 스캔 (연매출 대시보드)
        @Index(name = "IX_SALES_YEARLY_YEAR", columnList = "SALES_YEAR")
    }
    */
)
public class SalesYearly {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "YEARLY_SALES_ID")
  private Long id;

  @Column(name = "STORE_TENANT_MAP_ID", nullable = false)
  private Long storeTenantMapId;

  @Column(name = "SALES_YEAR", nullable = false)
  private int salesYear;

  @Column(name = "TOTAL_SALES_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal totalSalesAmount = BigDecimal.ZERO;

  @Column(name = "TOTAL_SALES_COUNT", nullable = false)
  private int totalSalesCount = 0;

  @Column(name = "VIP_SALES_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal vipSalesAmount = BigDecimal.ZERO;

  @Column(name = "VIP_SALES_COUNT", nullable = false)
  private int vipSalesCount = 0;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT", nullable = false)
  private LocalDateTime updatedAt;

  protected SalesYearly() {
  }

  public SalesYearly(Long storeTenantMapId, int salesYear) {
    this.storeTenantMapId = storeTenantMapId;
    this.salesYear = salesYear;
    this.totalSalesAmount = BigDecimal.ZERO;
    this.vipSalesAmount = BigDecimal.ZERO;
    this.totalSalesCount = 0;
    this.vipSalesCount = 0;
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

  // DAILY/MONTHLY 집계 합산용
  public void addPeriod(BigDecimal totalAmount, int totalCnt, BigDecimal vipAmount, int vipCnt) {
    this.totalSalesAmount = this.totalSalesAmount.add(totalAmount);
    this.totalSalesCount += totalCnt;
    this.vipSalesAmount = this.vipSalesAmount.add(vipAmount);
    this.vipSalesCount += vipCnt;
  }

  public Long getId() { return id; }
  public Long getStoreTenantMapId() { return storeTenantMapId; }
  public int getSalesYear() { return salesYear; }
  public BigDecimal getTotalSalesAmount() { return totalSalesAmount; }
  public int getTotalSalesCount() { return totalSalesCount; }
  public BigDecimal getVipSalesAmount() { return vipSalesAmount; }
  public int getVipSalesCount() { return vipSalesCount; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
}
