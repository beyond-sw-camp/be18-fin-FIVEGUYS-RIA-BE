package com.fiveguys.RIA.RIA_Backend.campaign.model.entity;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "ESTIMATE")
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long estimateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
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

    public static Estimate create(
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
        return Estimate.builder()
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

