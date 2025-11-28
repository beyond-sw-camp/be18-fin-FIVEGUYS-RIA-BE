package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "STORE_ESTIMATE_MAP")
public class StoreEstimateMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_estimate_map_id", nullable = false, updatable = false)
    private Long storeEstimateMapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id", nullable = false)
    private Estimate estimate;

    @Column(name = "area_size", nullable = false)
    private Double areaSize;

    @Column(name = "rent_price", nullable = false)
    private Long rentPrice;

    @Column(name = "additional_fee", nullable = false)
    private Long additionalFee;

    @Column(name = "discount_amount", nullable = false)
    private Long discountAmount;

    @Column(name = "final_estimate_amount", nullable = false)
    private Long finalEstimateAmount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSpace(
            Long additionalFee,
            Long discountAmount,
            String description
    ) {
        if (additionalFee != null) this.additionalFee = additionalFee;
        if (discountAmount != null) this.discountAmount = discountAmount;
        if (description != null) this.description = description;
    }
}
