package com.fiveguys.RIA.RIA_Backend.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "ROLE",
    uniqueConstraints = @UniqueConstraint(name = "UK_ROLE_NAME", columnNames = "ROLE_NAME")
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ROLE_ID")
  private Long id;

  // ENUM('ROLE_ADMIN','ROLE_SALES_LEAD', 'ROLE_SALES_MEMBER')
  @Enumerated(EnumType.STRING)
  @Column(name = "ROLE_NAME", nullable = false, length = 50)
  private RoleName roleName;

  // ENUM
  @Enumerated(EnumType.STRING)
  @Column(name = "DEPARTMENT", length = 50)
  private Department department;

  @Column(name = "DESCRIPTION", length = 255)
  private String description;

  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  public enum RoleName {
    ROLE_ADMIN,
    ROLE_SALES_LEAD,
    ROLE_SALES_MEMBER
  }

  public enum Department {
    ADMIN,
    SALES,
  }
}
