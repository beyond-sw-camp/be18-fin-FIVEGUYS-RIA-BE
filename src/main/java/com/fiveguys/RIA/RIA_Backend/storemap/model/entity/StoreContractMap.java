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
import java.time.LocalDate;

@Entity
@Table(name = "STORE_CONTRACT_MAP")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreContractMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORE_CONTRACT_MAP_ID")
    private Long id;

    @Column(name = "STORE_ID")
    private Long storeId;

    @Column(name = "CONTRACT_START_DATE")
    private LocalDate contractStartDate;

    @Column(name = "CONTRACT_END_DATE")
    private LocalDate contractEndDate;

    @Column(name = "FINAL_CONTRACT_AMOUNT")
    private BigDecimal finalContractAmount;

    @Column(name = "COMMISSION_RATE")
    private BigDecimal commissionRate;
}
