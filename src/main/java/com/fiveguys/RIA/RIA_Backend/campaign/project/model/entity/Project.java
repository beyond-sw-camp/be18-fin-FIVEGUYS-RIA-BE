package com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

  private Integer expectedRevenue;
  private BigDecimal expectedMarginRate;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
  private Pipeline pipeline;

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Proposal> proposals = new ArrayList<>();

  public enum Type {
    POPUP, EXHIBITION, RENTAL
  }

  public enum Status {
    ACTIVE, COMPLETED, CANCELLED
  }

  //  도메인 업데이트 메서드 섹션

  /** 기본 정보 수정 (제목, 설명, 유형) */
  public void updateBasicInfo(String title, String description, Type type) {
    if (title != null && !title.isBlank()) this.title = title;
    if (description != null) this.description = description;
    if (type != null) this.type = type;
  }

  /** 매출/이익 관련 수정 */
  public void updateFinancial(Integer revenue, BigDecimal marginRate) {
    if (revenue != null) this.expectedRevenue = revenue;
    if (marginRate != null) this.expectedMarginRate = marginRate;
  }

  /** 기간 수정 */
  public void updatePeriod(LocalDate start, LocalDate end) {
    if (start != null) this.startDay = start;
    if (end != null) this.endDay = end;
  }

  /** 상태 변경 */
  public void updateStatus(Status status) {
    if (status != null) this.status = status;
  }

  /** 편의 메서드: DTO 전체 적용용 */
  public void applyPatch(
      String title,
      String description,
      Type type,
      Integer expectedRevenue,
      BigDecimal expectedMarginRate,
      LocalDate startDay,
      LocalDate endDay
  ) {
    updateBasicInfo(title, description, type);
    updateFinancial(expectedRevenue, expectedMarginRate);
    updatePeriod(startDay, endDay);
  }
}
