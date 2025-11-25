package com.fiveguys.RIA.RIA_Backend.facility.store.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private Long storeId;

    @Column(name = "floor_id", nullable = false)
    private Long floorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreType type;   // REGULAR / POPUP

    @Column(name = "store_number", nullable = false, length = 100)
    private String storeNumber;   // 호수

    @Column(name = "area_size")
    private Double areaSize;      // 면적 (NULL 가능)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;   // AVAILABLE / OCCUPIED / RESERVED / MAINTENANCE

    @Column(name = "rent_price")
    private Long rentPrice;       // 기본 임대료 (NULL 가능)

    @Column(name = "description", length = 255)
    private String description;   // 설명

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 등록일

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정일


    // ENUM 정의
    public enum StoreType {
        REGULAR, POPUP, EXHIBITION
    }

    public enum StoreStatus {
        AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE
    }

}
