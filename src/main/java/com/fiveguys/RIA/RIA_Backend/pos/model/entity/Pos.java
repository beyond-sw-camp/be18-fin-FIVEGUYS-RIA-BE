package com.fiveguys.RIA.RIA_Backend.pos.model.entity;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "POS"/*,
    indexes = { //성능 테스트 할때 쓸꺼라 지우지 마세요
        @Index(name = "IX_POS_STORE_DATE", columnList = "STORE_ID, PURCHASE_AT"),
        @Index(name = "IX_POS_TENANT_DATE", columnList = "STORE_TENANT_MAP_ID, PURCHASE_AT"),
        @Index(name = "IX_POS_CUSTOMER_DATE", columnList = "CUSTOMER_ID, PURCHASE_AT")
    }*/
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Pos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POS_ID")
    private Long posId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID", nullable = false)
    private Store store;

    //여기에 컬럼이 join으로 붙으면
    //private storetenantmap storetenantmap 이렇게 바뀌어야합니다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_TENANT_MAP_ID", nullable = false)
    private StoreTenantMap storeTenantMap;


    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Column(name = "BRAND_NAME", nullable = false, length = 50)
    private String brandName;

    @Column(name = "PRODUCT_NAME", length = 50)
    private String productName;

    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "PURCHASE_AT", nullable = false)
    private LocalDateTime purchaseAt;

//    @Column(name = "CREATED_AT", nullable = false)
//    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (purchaseAt == null) purchaseAt = LocalDateTime.now();
    }

    // getter 필요에 따라 추가
    public Long getPosId() {
        return posId;
    }

    public Long getStoreId() {
        return store != null ? store.getStoreId() : null;
    }

    public Long getStoreTenantMapId() {
        return storeTenantMap != null ? storeTenantMap.getStoreTenantMapId() : null;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getPurchaseAt() {
        return purchaseAt;
    }

/*    public LocalDateTime getCreatedAt() {
        return createdAt;
    }*/
}
