package com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity;

import com.fiveguys.RIA.RIA_Backend.facility.zone.model.entity.Zone;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "FLOOR")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_id", nullable = false, updatable = false)
    private Long floorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Enumerated(EnumType.STRING)
    @Column(name = "floor_name", nullable = false, length = 10)
    private FloorName floorName;   // B1, 1F, 2F ... 4F

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private Category category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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

    public enum FloorName {
        B1, F1, F2, F3, F4
    }

    public enum Category {
        HIGH_JEWELRY_WATCH,
        LUXURY_BOUTIQUE_COSMETICS,
        LUXURY_BOUTIQUE_WOMEN_COLLECTION,
        WOMEN_COLLECTION,
        MEN_LUXURY_BOUTIQUE;
    }
}
