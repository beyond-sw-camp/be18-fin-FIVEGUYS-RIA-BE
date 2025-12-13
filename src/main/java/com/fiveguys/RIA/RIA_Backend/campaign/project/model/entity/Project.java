package com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "PROJECT")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long projectId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sales_manager_id", nullable = false)
  private User salesManager;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_company_id", nullable = false)
  private ClientCompany clientCompany;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  private String title;

  @Enumerated(EnumType.STRING)
  private Type type; // POPUP, EXHIBITION, RENTAL

  @Enumerated(EnumType.STRING)
  private Status status; // ACTIVE, COMPLETED, CANCELLED

  private String description;

  private LocalDate startDay;
  private LocalDate endDay;

  private BigDecimal expectedRevenue;
  private BigDecimal expectedMarginRate;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
  private Pipeline pipeline;

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Proposal> proposals = new ArrayList<>();

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Estimate> estimates = new ArrayList<>();

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
  private List<Contract> contracts = new ArrayList<>();

  @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
  private Revenue revenue;


  public enum Type {
    POPUP, EXHIBITION, RENTAL
  }

  public enum Status {
    ACTIVE, COMPLETED, CANCELLED
  }

  // 생성 팩토리
  public static Project create(
      User manager,
      ClientCompany company,
      Client client,
      String title,
      Type type,
      String description,
      LocalDate startDay,
      LocalDate endDay,
      BigDecimal expectedRevenue,
      BigDecimal expectedMarginRate
  ) {
    return Project.builder()
        .salesManager(manager)
        .clientCompany(company)
        .client(client)
        .title(title)
        .type(type)
        .description(description)
        .status(Status.ACTIVE)
        .startDay(startDay)
        .endDay(endDay)
        .expectedRevenue(expectedRevenue)
        .expectedMarginRate(expectedMarginRate)
        .build();
  }

  // Proposal 스타일 update 통합 버전
  public void update(
      String newTitle,
      String newDescription,
      Type newType,
      BigDecimal newExpectedRevenue,
      BigDecimal newExpectedMarginRate,
      LocalDate newStartDay,
      LocalDate newEndDay
  ) {
    if (newTitle != null && !newTitle.isBlank()) this.title = newTitle;
    if (newDescription != null) this.description = newDescription;
    if (newType != null) this.type = newType;

    if (newExpectedRevenue != null) this.expectedRevenue = newExpectedRevenue;
    if (newExpectedMarginRate != null) this.expectedMarginRate = newExpectedMarginRate;

    if (newStartDay != null) this.startDay = newStartDay;
    if (newEndDay != null) this.endDay = newEndDay;
  }

  public void updateSalesManager(User newManager) {
    this.salesManager = newManager;
  }
  // 전체 취소
  public void cancel() {
    this.status = Status.CANCELLED;

    if (this.pipeline != null) {
      this.pipeline.cancel();
    }

    if (this.proposals != null && !this.proposals.isEmpty()) {
      this.proposals.forEach(Proposal::cancel);
    }

    // 추후에 이거 추가해야할듯
    // estimates.forEach(Estimate::cancel)
    // contracts.forEach(Contract::cancel)
    // revenues.forEach(Revenue::cancel)
  }

  public void complete() {
    this.status = Status.COMPLETED;
  }
}
