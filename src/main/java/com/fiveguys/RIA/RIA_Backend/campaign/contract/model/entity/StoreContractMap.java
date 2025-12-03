package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "store_contract_map")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StoreContractMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_contract_map_id", nullable = false, updatable = false)
    private Long storeContractMapId;

    // 매장(공간)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // 연관 계약
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    // areaSize, rentPrice는 Store 객체가 정보를 가지고 있는데 매핑 엔티티에도 가지고 있을 필요가 있나? 궁금
    @Column(name = "area_size", nullable = false)
    private Double areaSize;

    // 임대료, erd는 Demical인데 견적과 통일
    @Column(name = "rent_price", nullable = false)
    private Long rentPrice;

    @Column(name = "additional_fee", nullable = false)
    private Long additionalFee;

    @Column(name = "discount_amount", nullable = false)
    private Long discountAmount;

    // 계약 확정 금액 (견적 금액 기반) 견적과 Long 통일
    @Column(name = "final_contract_amount")
    private Long finalContractAmount;

    // 비고
    @Column(name = "description", length = 255)
    private String description;

    // 등록일
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정일
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // 엔티티 생성
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
}
