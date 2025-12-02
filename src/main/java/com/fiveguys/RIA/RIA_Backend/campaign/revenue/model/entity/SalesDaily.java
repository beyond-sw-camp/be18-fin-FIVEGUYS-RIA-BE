package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "SALES_DAILY"
    /*
    ,indexes = {
        // 매장(공간+브랜드) + 날짜로 일매출 조회 / 정산용
        @Index(name = "IX_SALES_DAILY_TENANT_DATE", columnList = "STORE_TENANT_MAP_ID, SALES_DATE"),
        // 특정 날짜 기준 전체 매장 스캔 (일매출 대시보드)
        @Index(name = "IX_SALES_DAILY_DATE", columnList = "SALES_DATE")
    }
    */
)
public class SalesDaily {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "DAILY_SALES_ID")
  private Long salesDailyId;

  @Column(name = "STORE_TENANT_MAP_ID", nullable = false)
  private Long storeTenantMapId;      // 매장+브랜드+계약 매핑 ID

  @Column(name = "SALES_DATE", nullable = false)
  private LocalDate salesDate;        // 매출 일자

  @Column(name = "TOTAL_SALES_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal totalSalesAmount = BigDecimal.ZERO;  // 총 매출 금액

  @Column(name = "TOTAL_SALES_COUNT", nullable = false)
  private int totalSalesCount = 0;    // 총 거래 건수

  @Column(name = "VIP_SALES_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal vipSalesAmount = BigDecimal.ZERO;    // VIP 매출 금액

  @Column(name = "VIP_SALES_COUNT", nullable = false)
  private int vipSalesCount = 0;      // VIP 거래 건수

  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;

  protected SalesDaily() {
  }

  public SalesDaily(Long storeTenantMapId, LocalDate salesDate) {
    this.storeTenantMapId = storeTenantMapId;
    this.salesDate = salesDate;
    this.totalSalesAmount = BigDecimal.ZERO;
    this.vipSalesAmount = BigDecimal.ZERO;
    this.totalSalesCount = 0;
    this.vipSalesCount = 0;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }


  @PrePersist
  void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now(); //생성할때도 그냥 넣어버림
  }
  
  @PreUpdate
  void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  // 집계용 메서드
  public void increaseTotal(BigDecimal amount) {
    this.totalSalesAmount = this.totalSalesAmount.add(amount);
    this.totalSalesCount += 1;
  }

  public void increaseVip(BigDecimal amount) {
    this.vipSalesAmount = this.vipSalesAmount.add(amount);
    this.vipSalesCount += 1;
  }

  // getter
  public Long getSalesDailyId() {
    return salesDailyId;
  }

  public Long getStoreTenantMapId() {
    return storeTenantMapId;
  }

  public LocalDate getSalesDate() {
    return salesDate;
  }

  public BigDecimal getTotalSalesAmount() {
    return totalSalesAmount;
  }

  public int getTotalSalesCount() {
    return totalSalesCount;
  }

  public BigDecimal getVipSalesAmount() {
    return vipSalesAmount;
  }

  public int getVipSalesCount() {
    return vipSalesCount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}
