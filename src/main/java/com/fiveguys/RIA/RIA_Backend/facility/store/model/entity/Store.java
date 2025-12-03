package com.fiveguys.RIA.RIA_Backend.facility.store.model.entity;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "STORE")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id", nullable = false, updatable = false)
    private Long storeId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private StoreType type;     // REGULAR, POPUP, EXHIBITION

    @Column(name = "store_number", nullable = false, length = 100)
    private String storeNumber; // 호수 (예: 101호)

    @Column(name = "area_size")
    private Double areaSize;    // 면적 m²

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StoreStatus status; // AVAILABLE / OCCUPIED / RESERVED / MAINTENANCE

    @Column(name = "rent_price")
    private Long rentPrice;     // 기본 임대료

    @Column(name = "description", length = 255)
    private String description; // 매장 설명

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ====== ENUM ======
    public enum StoreType {
        REGULAR, POPUP, EXHIBITION
    }

    public enum StoreStatus {
        AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE
    }

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

}
