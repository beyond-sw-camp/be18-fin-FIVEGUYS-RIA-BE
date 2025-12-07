package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
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
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CONTRACT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id", nullable = false, updatable = false)
    private Long contractId;

    // 연관 프로젝트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    // 연관 파이프라인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id")
    private Pipeline pipeline;

    // 생성한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user", nullable = false)
    private User createdUser;

    // 고객사
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_company_id", nullable = false)
    private ClientCompany clientCompany;

    // 담당자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // 연관 견적
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;

    // 계약 유형 (LEASE, CONSIGNMENT, MIX)
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractType contractType;

    // 계약명
    @Column(name = "contract_title", nullable = false, length = 255)
    private String contractTitle;

    // 계약 보증금
    @Column(name = "contract_amount", nullable = false)
    private Long contractAmount;

    // 수수료율
    @Column(name = "commission_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal commissionRate;

    // 화폐 단위
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    // 계약 시작일
    @Column(name = "contract_start_date", nullable = false)
    private LocalDate contractStartDate;

    // 계약 종료일
    @Column(name = "contract_end_date", nullable = false)
    private LocalDate contractEndDate;

    // 계약 체결일
    @Column(name = "contract_date", nullable = false)
    private LocalDate contractDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_condition", length = 255)
    private PaymentCondition paymentCondition;

    // 임대료 계산 방식 (MONTHLY, YEARLY, FIXED)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "rent_type", nullable = false)
    private RentType rentType = RentType.MONTHLY;

    // 계약 상태 (SUBMITTED, COMPLETED, CANCELED)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.SUBMITTED;

    // 비고
    @Lob
    @Column(name = "remark")
    private String remark;

    // 총 금액 (StoreContractMap의 total의 값을 전부 더한 금액)
    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    // 생성일
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정일
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreContractMap> storeContractMaps = new ArrayList<>();

    // 계약 유형
    public enum ContractType {
        LEASE, CONSIGNMENT, MIX
    }

    // 임대료 계산 방식
    public enum RentType {
        MONTHLY, YEARLY, FIXED
    }

    // 계약 상태
    public enum Status {
        SUBMITTED, COMPLETED, CANCELLED
    }

    // 화폐 단위
    public enum Currency {
        KRW, USD, EUR
    }

    // 지급 조건, erd에는 varchar인데 견적이랑 통일
    public enum PaymentCondition {
        PREPAY, POSTPAY
    }

    // 엔티티 생성
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // 엔티티 수정
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    //
    public void complete() {
        this.status = Contract.Status.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    // 계약 취소 처리
    public void cancel() {
        this.status = Contract.Status.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    // 총액 업데이트
    public void updateTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal calcBaseRent(YearMonth ym) {
        YearMonth startYm = YearMonth.from(this.contractStartDate);
        YearMonth endYm = YearMonth.from(this.contractEndDate);

        if (ym.isBefore(startYm) || ym.isAfter(endYm)) {
            return BigDecimal.ZERO;
        }

        BigDecimal amount = BigDecimal.valueOf(this.contractAmount);

        switch (this.rentType) {

            case MONTHLY:
                return amount;

            case YEARLY:
                if (ym.getMonthValue() == startYm.getMonthValue()) {
                    return amount;
                }
                return BigDecimal.ZERO;

            case FIXED:
                long months = ChronoUnit.MONTHS.between(startYm, endYm) + 1;
                return amount.divide(BigDecimal.valueOf(months), 0, RoundingMode.HALF_UP);

            default:
                throw new IllegalStateException("unsupported rent type");
                // 여기에 이제 contracterrorcode 넣어야하는데 나중에 건드리겠습니다
        }
    }

    public void update(
            String contractTitle,
            Contract.Currency currency,
            Long contractAmount,
            BigDecimal commissionRate,
            Contract.ContractType contractType,
            String paymentCondition,
            LocalDate contractStartDate,
            LocalDate contractEndDate,
            LocalDate contractDate,
            String remark,
            Project project,
            Pipeline pipeline,
            Client client,
            ClientCompany clientCompany,
            Estimate estimate
    ) {
        this.contractTitle = contractTitle;
        this.currency = currency;
        this.contractAmount = contractAmount;
        this.commissionRate = commissionRate;
        this.contractType = contractType;
        this.paymentCondition = Contract.PaymentCondition.valueOf(paymentCondition);
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.contractDate = contractDate;
        this.remark = (remark != null) ? remark : "";
        this.project = project;
        this.pipeline = pipeline;
        this.client = client;
        this.clientCompany = clientCompany;
        this.estimate = estimate;

        this.updatedAt = LocalDateTime.now();
    }
}
