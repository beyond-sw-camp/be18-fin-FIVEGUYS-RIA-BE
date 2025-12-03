package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "ESTIMATE")
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_id", nullable = false, updatable = false)
    private Long estimateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id")
    private Pipeline pipeline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user", nullable = false)
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_company_id", nullable = false)
    private ClientCompany clientCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @Column(name = "estimate_title", nullable = false, length = 255)
    private String estimateTitle;

    @Column(name = "estimate_date", nullable = false)
    private LocalDate estimateDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_condition", nullable = false)
    private PaymentCondition paymentCondition;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Lob
    @Column(name = "remark")
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

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

    public enum Status {
        DRAFT, SUBMITTED, COMPLETED, CANCELED
    }

    public enum PaymentCondition {
        PREPAY, POSTPAY
    }

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreEstimateMap> storeEstimateMaps = new ArrayList<>();


    // 견적 삭제를 위한 메소드
    public void cancel() {
        this.status = Estimate.Status.CANCELED;
    }

    // 업데이트

    public void update(
            String estimateTitle,
            LocalDate estimateDate,
            LocalDate deliveryDate,
            String remark,
            Project newProject,
            ClientCompany newCompany,
            Client newClient
    ) {

        if (estimateTitle != null) this.estimateTitle = estimateTitle;
        if (estimateDate != null) this.estimateDate = estimateDate;
        if (deliveryDate != null) this.deliveryDate = deliveryDate;
        if (remark != null) this.remark = remark;

        if (newProject != null) this.project = newProject;
        if (newCompany != null) this.clientCompany = newCompany;
        if (newClient != null) this.client = newClient;
    }

    public void complete() {
        this.status = Estimate.Status.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
}
