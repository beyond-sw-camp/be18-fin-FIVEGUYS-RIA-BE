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
    name = "STORE_SALES_STATS"
    /*
    ,indexes = {
        // 특정 매장(테넌트) 기준으로 누적 통계 조회
        @Index(name = "IX_STORE_SALES_STATS_TENANT", columnList = "STORE_TENANT_MAP_ID"),
        // VIP 비중 높은/낮은 매장 랭킹 조회용
        @Index(name = "IX_STORE_SALES_STATS_VIP_RATIO", columnList = "VIP_RATIO")
    }
    */
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreSalesStats {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "STORE_SALES_STATS_ID")
  private Long storeSalesStatsId;

  @Column(name = "STORE_TENANT_MAP_ID", nullable = false)
  private Long storeTenantMapId;

  @Column(name = "TOTAL_PURCHASE_COUNT", nullable = false)
  private int totalPurchaseCount = 0;  // 전체 거래 횟수

  @Column(name = "TOTAL_SALES_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal totalSalesAmount = BigDecimal.ZERO; // 총 매출 금액

  @Column(name = "VIP_PURCHASE_COUNT", nullable = false)
  private int vipPurchaseCount = 0;    // VIP 거래 횟수

  @Column(name = "VIP_SALES_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal vipSalesAmount = BigDecimal.ZERO;   // VIP 매출 금액

  @Column(name = "NON_VIP_PURCHASE_COUNT", nullable = false)
  private int nonVipPurchaseCount = 0; // 일반 고객 거래 횟수

  @Column(name = "NON_VIP_SALES_AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal nonVipSalesAmount = BigDecimal.ZERO; // 일반 고객 매출 금액

  @Column(name = "VIP_RATIO", nullable = false, precision = 5, scale = 2)
  private BigDecimal vipRatio = BigDecimal.ZERO;         // VIP 매출 비중(%)

  @Column(name = "LAST_PURCHASE_AT")
  private LocalDateTime lastPurchaseAt;                  // 최근 구매일시

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "LAST_UPDATED_AT", nullable = false)
  private LocalDateTime lastUpdatedAt;

  public StoreSalesStats(Long storeTenantMapId) {
    this.storeTenantMapId = storeTenantMapId;
    this.totalSalesAmount = BigDecimal.ZERO;
    this.vipSalesAmount = BigDecimal.ZERO;
    this.nonVipSalesAmount = BigDecimal.ZERO;
    this.vipRatio = BigDecimal.ZERO;
  }

  @PrePersist
  void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.lastUpdatedAt = LocalDateTime.now();
  }

  @PreUpdate
  void onUpdate() {
    this.lastUpdatedAt = LocalDateTime.now();
  }

  // 집계 갱신 메서드들
  public void increaseVip(BigDecimal amount, LocalDateTime purchaseAt) {
    this.vipPurchaseCount += 1;
    this.vipSalesAmount = this.vipSalesAmount.add(amount);
    this.totalPurchaseCount += 1;
    this.totalSalesAmount = this.totalSalesAmount.add(amount);
    this.lastPurchaseAt = purchaseAt;
    recalcVipRatio();
  }

  public void increaseNonVip(BigDecimal amount, LocalDateTime purchaseAt) {
    this.nonVipPurchaseCount += 1;
    this.nonVipSalesAmount = this.nonVipSalesAmount.add(amount);
    this.totalPurchaseCount += 1;
    this.totalSalesAmount = this.totalSalesAmount.add(amount);
    this.lastPurchaseAt = purchaseAt;
    recalcVipRatio();
  }

  private void recalcVipRatio() {
    if (this.totalSalesAmount.compareTo(BigDecimal.ZERO) > 0) {
      this.vipRatio = this.vipSalesAmount
          .multiply(BigDecimal.valueOf(100))
          .divide(this.totalSalesAmount, 2, java.math.RoundingMode.HALF_UP);
    } else {
      this.vipRatio = BigDecimal.ZERO;
    }
  }

  public Long getStoreSalesStatsId() { return storeSalesStatsId; }
  public Long getStoreTenantMapId() { return storeTenantMapId; }
  public int getTotalPurchaseCount() { return totalPurchaseCount; }
  public BigDecimal getTotalSalesAmount() { return totalSalesAmount; }
  public int getVipPurchaseCount() { return vipPurchaseCount; }
  public BigDecimal getVipSalesAmount() { return vipSalesAmount; }
  public int getNonVipPurchaseCount() { return nonVipPurchaseCount; }
  public BigDecimal getNonVipSalesAmount() { return nonVipSalesAmount; }
  public BigDecimal getVipRatio() { return vipRatio; }
  public LocalDateTime getLastPurchaseAt() { return lastPurchaseAt; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
}
