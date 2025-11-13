package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "PROPOSAL")
public class Proposal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long proposalId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pipeline_id")
  private Pipeline pipeline;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_user", nullable = false)
  private User createdUser;  // 담당자: 필수

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;  // 고객: 필수

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_company_id", nullable = false)
  private ClientCompany clientCompany; // 고객사: 필수

  @Column(nullable = false)
  private String title; // 제안명: 필수

  @Lob
  private String data;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "submit_date", nullable = false)
  private LocalDate submitDate; // 제출일: 필수

  private LocalDate requestDate;
  private LocalDate presentDate;
  private LocalDate periodStart;
  private LocalDate periodEnd;

  public enum Status {
    DRAFT, SUBMITTED, COMPLETED, REJECTED, CANCELED
  }

  public void cancel() {
    this.status = Status.CANCELED;
  }
}

