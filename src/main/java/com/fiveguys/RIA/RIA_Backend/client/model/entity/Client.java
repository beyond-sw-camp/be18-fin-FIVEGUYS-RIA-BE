package com.fiveguys.RIA.RIA_Backend.client.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "CLIENT",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_CLIENT_EMAIL",
            columnNames = "EMAIL"
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CLIENT_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CLIENT_COMPANY_ID", nullable = false)
  private ClientCompany clientCompany;

  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @Column(name = "POSITION", length = 100)
  private String position;

  @Column(name = "EMAIL", length = 255)
  private String email;

  @Column(name = "PHONE", length = 50)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false, length = 20)
  private Type type;

  @Column(name = "IS_DELETED", nullable = false)
  private boolean isDeleted;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATE_AT", nullable = false)
  private LocalDateTime updatedAt;

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

  @Builder
  public Client(ClientCompany clientCompany, String name, String position,
      String email, String phone, Type type) {
    this.clientCompany = clientCompany;
    this.name = name;
    this.position = position;
    this.email = email;
    this.phone = phone;
    this.type = type;
    this.isDeleted = false;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if (clientCompany != null) {
      clientCompany.getClients().add(this);
    }
  }
}
