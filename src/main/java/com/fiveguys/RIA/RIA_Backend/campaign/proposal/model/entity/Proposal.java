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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "PROPOSAL")
public class Proposal {

  @Id
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
  private String data;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "submit_date", nullable = false)
  private LocalDate submitDate;

  private LocalDate requestDate;

  @Lob
  private String remark;

  public enum Status {
    DRAFT, SUBMITTED, COMPLETED, CANCELED
  }

  public static Proposal create(
      Project project,
      Pipeline pipeline,
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
    return Proposal.builder()
        .project(project)
        .pipeline(pipeline)
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

    if (newProject != null) this.project = newProject;
    if (newCompany != null) this.clientCompany = newCompany;
    if (newClient != null) this.client = newClient;
  }
}
