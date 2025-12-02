package com.fiveguys.RIA.RIA_Backend.ai.model.entity;

import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Table(name = "Ai")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Ai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AI_ID")
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "VIP_ID", nullable = false)
    private Vip vip;

    @Column (name = "RECO_TYPE", length = 20, nullable = false)
    private String recoType;

    @Column(name = "TARGET_NAME", length = 100, nullable = false)
    private String targetName;

    @Column(name = "SCORE", precision = 15, scale = 2, nullable = false)
    private BigDecimal score;

    @Column(name = "REASON", length = 500)
    private String reason;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
