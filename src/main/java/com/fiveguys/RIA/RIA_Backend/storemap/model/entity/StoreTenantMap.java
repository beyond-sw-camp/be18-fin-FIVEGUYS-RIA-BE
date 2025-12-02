package com.fiveguys.RIA.RIA_Backend.storemap.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "STORE_TENANT_MAP")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreTenantMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORE_TENANT_MAP_ID")
    private Long id;

    @Column(name = "STORE_ID")
    private Long storeId;

    @Column(name = "STORE_DISPLAY_NAME")
    private String storeDisplayName;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    public enum Status {
        ACTIVE, ENDED, RESERVED
    }
}
