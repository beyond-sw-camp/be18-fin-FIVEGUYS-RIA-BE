package com.fiveguys.RIA.RIA_Backend.client.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "CLIENT_COMPANY",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_CLIENT_COMPANY_BUSINESS_NUMBER",
            columnNames = "BUSINESS_NUMBER"
        ),
        @UniqueConstraint(
            name = "UK_CLIENT_COMPANY_COMPANY_NAME",
            columnNames = "COMPANY_NAME"
        ),
        @UniqueConstraint(
            name = "UK_CLIENT_COMPANY_WEBSITE",
            columnNames = "WEBSITE"
        )
    }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientCompany {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CLIENT_COMPANY_ID")
  private Long id;

  @Column(name = "COMPANY_NAME", nullable = false, length = 255)
  private String companyName;

  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", nullable = false, length = 50)
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false, length = 20)
  private Type type;

  @Column(name = "BUSINESS_NUMBER", length = 50)
  private String businessNumber;

  @Column(name = "PHONE", length = 50)
  private String phone;

  @Column(name = "ADDRESS", length = 255)
  private String address;

  @Column(name = "WEBSITE", length = 255)
  private String website;

  @Column(name = "FAX", length = 50)
  private String fax;

  @Column(name = "ZIP_CODE", length = 50)
  private String zipCode;

  @Column(name = "IS_DELETED", nullable = false)
  private boolean isDeleted;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "clientCompany", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Client> clients = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.isDeleted = false;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public enum Type { LEAD, CUSTOMER }
}
