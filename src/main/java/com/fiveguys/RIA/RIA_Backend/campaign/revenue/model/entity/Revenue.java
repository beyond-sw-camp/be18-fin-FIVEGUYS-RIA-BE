/*
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
    name = "REVENUE"
    */
/*
    ,indexes = {
        // 프로젝트 단위 매출 조회
        @Index(name = "IX_REVENUE_PROJECT", columnList = "PROJECT_ID"),
        // 계약 단위 매출 조회
        @Index(name = "IX_REVENUE_CONTRACT", columnList = "CONTRACT_ID"),
        // 고객사 기준 매출 조회
        @Index(name = "IX_REVENUE_CLIENT_COMPANY", columnList = "CLIENT_COMPANY_ID")
    }
    *//*

)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Revenue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "REVENUE_ID")
  private Long revenueId;

  @Column(name = "PROJECT_ID", nullable = false)
  private Long projectId;

  @Column(name = "CLIENT_COMPANY_ID", nullable = false)
  private Long clientCompanyId;

  @Column(name = "CLIENT_ID", nullable = false)
  private Long clientId;

  @Column(name = "CONTRACT_ID", nullable = false)
  private Long contractId;

  @Column(name = "PIPELINE_ID", nullable = false)
  private Long pipelineId;

  @Column(name = "CREATED_USER", nullable = false)
  private Long createdUserId;

  // 계약 당시 스냅샷
  @Column(name = "COMMISSION_RATE_SNAPSHOT", nullable = false, precision = 5, scale = 2)
  private BigDecimal commissionRateSnapshot;

  @Column(name = "BASE_RENT_SNAPSHOT", nullable = false)
  private Integer baseRentSnapshot;

  @Column(name = "REMARK")
  private String remark;

  public enum RevenueStatus {
    ACTIVE,
    COMPLETED,
    CANCELED
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 20)
  private RevenueStatus status;

  // 지금까지 누적된 정산 금액 (FINAL_REVENUE 합계)
  @Column(name = "TOTAL_PRICE", nullable = false, precision = 15, scale = 2)
  private BigDecimal totalPrice = BigDecimal.ZERO;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT", nullable = false)
  private LocalDateTime updatedAt;


  public Revenue(Long projectId,
      Long clientCompanyId,
      Long clientId,
      Long contractId,
      Long pipelineId,
      Long createdUserId,
      BigDecimal commissionRateSnapshot,
      Integer baseRentSnapshot) {
    this.projectId = projectId;
    this.clientCompanyId = clientCompanyId;
    this.clientId = clientId;
    this.contractId = contractId;
    this.pipelineId = pipelineId;
    this.createdUserId = createdUserId;
    this.commissionRateSnapshot = commissionRateSnapshot;
    this.baseRentSnapshot = baseRentSnapshot;
    this.status = RevenueStatus.ACTIVE;
    this.totalPrice = BigDecimal.ZERO;
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

  // 정산 배치에서 누적값 갱신용
  public void updateTotalPrice(BigDecimal newTotalPrice) {
    this.totalPrice = newTotalPrice;
  }

  public void changeStatus(RevenueStatus status) {
    this.status = status;
  }

  public Long getRevenueId() { return revenueId; }
  public Long getProjectId() { return projectId; }
  public Long getClientCompanyId() { return clientCompanyId; }
  public Long getClientId() { return clientId; }
  public Long getContractId() { return contractId; }
  public Long getPipelineId() { return pipelineId; }
  public Long getCreatedUserId() { return createdUserId; }
  public BigDecimal getCommissionRateSnapshot() { return commissionRateSnapshot; }
  public Integer getBaseRentSnapshot() { return baseRentSnapshot; }
  public String getRemark() { return remark; }
  public RevenueStatus getStatus() { return status; }
  public BigDecimal getTotalPrice() { return totalPrice; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
*/
