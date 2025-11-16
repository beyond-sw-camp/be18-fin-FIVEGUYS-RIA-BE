package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
  private Integer currentStage;  // 숫자 기반 단계 (1 ~ 5)

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StageName stageName;   // 단계 이름 ENUM

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;


  // ─────────────────────────────────────
  // ENUM 정의
  // ─────────────────────────────────────

  public enum Status {
    ACTIVE,
    COMPLETED,
    CANCELED
  }

  public enum StageName {

    PROPOSAL_RECEIVED("제안수신", 1),   // 1단계
    INTERNAL_REVIEW("내부검토", 2),     // 2단계
    ESTIMATE("견적", 3),                // 3단계
    NEGOTIATION("협상", 4),             // 4단계
    CONTRACT_SUCCESS("계약성공", 5);    // 5단계

    private final String displayName;
    private final int stageNo;

    StageName(String displayName, int stageNo) {
      this.displayName = displayName;
      this.stageNo = stageNo;
    }

    public String getDisplayName() {
      return displayName;
    }

    public int getStageNo() {
      return stageNo;
    }

    // 숫자 단계 → ENUM 변환
    public static StageName fromStageNo(int stageNo) {
      for (StageName s : values()) {
        if (s.stageNo == stageNo) return s;
      }
      throw new IllegalArgumentException("Invalid stage number: " + stageNo);
    }
  }


  // ─────────────────────────────────────
  // 메서드 구분 (자동 / 수동)
  // ─────────────────────────────────────

  /**
   * 자동 단계 상승 (내부 비즈니스 로직 전용)
   * - 하강 금지
   * - enum/status 직접 지정
   */
  public void autoAdvance(int newStage, StageName newStageName, Status newStatus) {
    if (newStage <= 0) return;

    if (this.currentStage == null || this.currentStage < newStage) {
      this.currentStage = newStage;

      if (newStageName != null) {
        this.stageName = newStageName;
      }

      if (newStatus != null) {
        this.status = newStatus;
      }
    }
  }

  /**
   * UI에서 사용자가 클릭해서 바꾸는 수동 변경
   * - 하강 허용
   * - StageName 자동 매핑
   * - 5단계면 상태 COMPLETED 처리
   */
  public void updateStage(int targetStage) {
    this.currentStage = targetStage;
    this.stageName = StageName.fromStageNo(targetStage);

    if (targetStage == 5) {
      this.status = Status.COMPLETED;
    }
  }


  /**
   * 취소 처리
   */
  public void cancel() {
    this.status = Status.CANCELED;
  }
}
