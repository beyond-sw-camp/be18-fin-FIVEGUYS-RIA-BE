//package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity;
//
//import jakarta.persistence.Entity;
//
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(
//    name = "POS"/*,
//    indexes = {
//        @Index(name = "IX_POS_STORE_DATE", columnList = "STORE_ID, PURCHASE_AT"),
//        @Index(name = "IX_POS_TENANT_DATE", columnList = "STORE_TENANT_MAP_ID, PURCHASE_AT"),
//        @Index(name = "IX_POS_CUSTOMER_DATE", columnList = "CUSTOMER_ID, PURCHASE_AT")
//    }*/
//)
//public class Pos {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "POS_ID")
//  private Long posId;
//
//  @Column(name = "STORE_ID", nullable = false)
//  private Long storeId;              // 매장(공간) FK
//
//  @Column(name = "STORE_TENANT_MAP_ID", nullable = false)
//  private Long storeTenantMapId;     // 매장+브랜드+계약 매핑 FK
//
//  @Column(name = "CUSTOMER_ID", nullable = false)
//  private Long customerId;           // 포스 시스템 고객 ID (VIP 매핑용)
//
//  @Column(name = "PRODUCT_NAME", length = 50)
//  private String productName;
//
//  @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2) //decimal 표현
//  private BigDecimal amount;        // 구매 금액
//
//  @Column(name = "PURCHASE_AT", nullable = false)
//  private LocalDateTime purchaseAt;  // 구매 시각
//
//  @Column(name = "CREATED_AT", nullable = false)
//  private LocalDateTime createdAt;   // 수신 시각(인서트 기준)
//
//  // getter 필요에 따라 추가
//  public Long getPosId() {
//    return posId;
//  }
//
//  public Long getStoreId() {
//    return storeId;
//  }
//
//  public Long getStoreTenantMapId() {
//    return storeTenantMapId;
//  }
//
//  public Long getCustomerId() {
//    return customerId;
//  }
//
//  public String getProductName() {
//    return productName;
//  }
//
//  public BigDecimal getAmount() {
//    return amount;
//  }
//
//  public LocalDateTime getPurchaseAt() {
//    return purchaseAt;
//  }
//
//  public LocalDateTime getCreatedAt() {
//    return createdAt;
//  }
//
//  public void setProductName(String productName) {
//    this.productName = productName;
//  }
//}
