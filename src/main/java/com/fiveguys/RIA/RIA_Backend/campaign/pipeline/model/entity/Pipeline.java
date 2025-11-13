package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PIPELINE")
public class Pipeline {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pipelineId;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @Column(nullable = true)
  private Integer currentStage;  // 숫자 기반 단계 (1~5)

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StageName stageName;   // 단계 이름 ENUM

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;         // ACTIVE, COMPLETED, REJECTED, INACTIVE

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  // ENUM 정의
  public enum Status {
    ACTIVE, COMPLETED, REJECTED, INACTIVE
  }

  public enum StageName {
    PROPOSAL_RECEIVED("제안수신"),   // 1단계
    INTERNAL_REVIEW("내부검토"),     // 2단계
    ESTIMATE("견적"),               // 3단계
    NEGOTIATION("협상"),            // 4단계
    CONTRACT_SUCCESS("계약성공");   // 5단계

    private final String displayName;

    StageName(String displayName) {
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }
  }
  public void updateStage(int newStage, StageName newStageName, Status newStatus) {
    if (newStage <= 0) return; // 잘못된 단계 무시

    // 현재 단계보다 높을 때만 갱신 (하강 금지)
    if (this.currentStage == null || this.currentStage < newStage) {
      this.currentStage = newStage;
      if (newStageName != null) this.stageName = newStageName;
      if (newStatus != null) this.status = newStatus;
    }
  }
}
