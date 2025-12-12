package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "PROPOSAL")
public class Proposal {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long proposalId;

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
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_company_id", nullable = false)
  private ClientCompany clientCompany;

  @Column(nullable = false)
  private String title;

  @Lob
  @Column
  private String data;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "submit_date", nullable = false)
  private LocalDate submitDate;

  @Column(name = "request_date", nullable = false)
  private LocalDate requestDate;

  @Lob
  private String remark;

  public enum Status {
    DRAFT, SUBMITTED, COMPLETED, CANCELED
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  public void changeProject(Project newProject) {
    this.project = newProject;
    this.pipeline = (newProject != null ? newProject.getPipeline() : null);
  }

  public static Proposal create(
      Project project,
      User createdUser,
      Client client,
      ClientCompany clientCompany,
      String title,
      String data,
      LocalDate requestDate,
      LocalDate submitDate,
      String remark,
      Status status
  ) {
    Proposal proposal = Proposal.builder()
        .createdUser(createdUser)
        .client(client)
        .clientCompany(clientCompany)
        .title(title)
        .data(data)
        .requestDate(requestDate)
        .submitDate(submitDate)
        .remark(remark)
        .status(status)
        .build();

    if (project != null) {
      proposal.changeProject(project); // project + pipeline 동시 세팅
    }

    return proposal;
  }


  public void cancel() {
    this.status = Status.CANCELED;
  }

  /**
   * Proposal 업데이트는 엔티티 스스로 관리하도록 한다.
   */
  public void update(
      String newTitle,
      String newData,
      LocalDate newRequestDate,
      LocalDate newSubmitDate,
      String newRemark,
      Project newProject,
      ClientCompany newCompany,
      Client newClient
  ) {

    if (newTitle != null) this.title = newTitle;
    if (newData != null) this.data = newData;
    if (newRequestDate != null) this.requestDate = newRequestDate;
    if (newSubmitDate != null) this.submitDate = newSubmitDate;
    if (newRemark != null) this.remark = newRemark;

    if (newProject != null) {
      this.changeProject(newProject); // project + pipeline 동기화
    }
    if (newCompany != null) this.clientCompany = newCompany;
    if (newClient != null) this.client = newClient;
  }

  public void complete() {
    this.status = Proposal.Status.COMPLETED;
  }

  public void changeStatus(Status newStatus) {
    this.status = newStatus;
  }
}
