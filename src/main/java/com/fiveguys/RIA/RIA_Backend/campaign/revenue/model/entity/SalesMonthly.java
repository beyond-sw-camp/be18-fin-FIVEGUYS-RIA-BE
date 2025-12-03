package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "SALES_MONTHLY"
    /*
    ,indexes = {
        // 정산(월 기준) + 월매출 대시보드
        @Index(name = "IX_SALES_MONTHLY_TENANT_MONTH", columnList = "STORE_TENANT_MAP_ID, SALES_YEAR, SALES_MONTH"),
        // 특정 월 전체 스캔
        @Index(name = "IX_SALES_MONTHLY_MONTH", columnList = "SALES_YEAR, SALES_MONTH")
    }
    */
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SalesMonthly {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MONTHLY_SALES_ID")
  private Long salesMonthlyId;

  @Column(name = "STORE_TENANT_MAP_ID", nullable = false)
  private Long storeTenantMapId;

  @Column(name = "SALES_YEAR", nullable = false)
  private int salesYear;

  @Column(name = "SALES_MONTH", nullable = false)
  private int salesMonth;

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

  public SalesMonthly(Long storeTenantMapId, int year, int month) {
    this.storeTenantMapId = storeTenantMapId;
    this.salesYear = year;
    this.salesMonth = month;
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


  public void reset() {
    this.totalSalesAmount = BigDecimal.ZERO;
    this.totalSalesCount = 0;
    this.vipSalesAmount = BigDecimal.ZERO;
    this.vipSalesCount = 0;
  }

  // 일별 집계 누적
  public void addDaily(BigDecimal totalAmount, int totalCnt,
      BigDecimal vipAmount, int vipCnt) {
    if (totalAmount != null) {
      this.totalSalesAmount = this.totalSalesAmount.add(totalAmount);
    }
    this.totalSalesCount += totalCnt;
    if (vipAmount != null) {
      this.vipSalesAmount = this.vipSalesAmount.add(vipAmount);
    }
    this.vipSalesCount += vipCnt;
  }

  public Long getSalesMonthlyId() { return salesMonthlyId; }
  public Long getStoreTenantMapId() { return storeTenantMapId; }
  public int getSalesYear() { return salesYear; }
  public int getSalesMonth() { return salesMonth; }
  public BigDecimal getTotalSalesAmount() { return totalSalesAmount; }
  public int getTotalSalesCount() { return totalSalesCount; }
  public BigDecimal getVipSalesAmount() { return vipSalesAmount; }
  public int getVipSalesCount() { return vipSalesCount; }
}
