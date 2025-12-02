package com.fiveguys.RIA.RIA_Backend.storemap.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "STORE_SALES_STATS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSalesStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORE_SALES_STATS_ID")
    private Long id;

    @Column(name = "STORE_TENANT_MAP_ID")
    private Long storeTenantMapId;

    @Column(name = "TOTAL_PURCHASE_COUNT")
    private Integer totalPurchaseCount;

    @Column(name = "TOTAL_SALES_AMOUNT")
    private BigDecimal totalSalesAmount;

    @Column(name = "VIP_PURCHASE_COUNT")
    private Integer vipPurchaseCount;

    @Column(name = "VIP_SALES_AMOUNT")
    private BigDecimal vipSalesAmount;

    @Column(name = "VIP_RATIO")
    private BigDecimal vipRatio;
}
