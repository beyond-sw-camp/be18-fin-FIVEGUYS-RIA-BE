package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "ESTIMATE")
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long estimateId;

    // --- 연관 관계 (project, pipeline 은 선택값) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")   // NULL 허용
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id")  // NULL 허용
    private Pipeline pipeline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user", nullable = false)
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_company_id", nullable = false)
    private ClientCompany clientCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // --- 기본 정보 ---
    @Column(name = "estimate_title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "estimate_date", nullable = false)
    private LocalDate estimateDate;   // 견적일

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_condition", nullable = false)
    private PaymentCondition paymentCondition;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;   // 납기일

    // --- 금액 ---
    @Column(nullable = false)
    private Long basePrice;

    @Column(nullable = false)
    private Long additionalPrice;

    @Column(nullable = false)
    private Long discountPrice;

    @Column(nullable = false)
    private Long totalPrice;

    @Lob
    private String remark;

    public enum Status {
        DRAFT, SUBMITTED, COMPLETED, CANCELED
    }
    // PaymentCondition (선불 후불) 필요한가? 
    public enum PaymentCondition {
        CASH, CARD
    }

    // --- Factory Method ---
    public static Estimate create(
            Project project,
            Pipeline pipeline,
            User createdUser,
            Client client,
            ClientCompany clientCompany,
            Store store,
            String title,
            Long basePrice,
            Long additionalPrice,
            Long discountPrice,
            LocalDate estimateDate,
            LocalDate deliveryDate,
            PaymentCondition paymentCondition,
            String remark,
            Status status
    ) {
        long total = basePrice + additionalPrice - discountPrice;

        return Estimate.builder()
                .project(project)
                .pipeline(pipeline)
                .createdUser(createdUser)
                .client(client)
                .clientCompany(clientCompany)
                .store(store)
                .title(title)
                .estimateDate(estimateDate)
                .deliveryDate(deliveryDate)
                .paymentCondition(paymentCondition)
                .basePrice(basePrice)
                .additionalPrice(additionalPrice)
                .discountPrice(discountPrice)
                .totalPrice(total)
                .remark(remark)
                .status(status)
                .build();
    }

    public void cancel() {
    }
}
