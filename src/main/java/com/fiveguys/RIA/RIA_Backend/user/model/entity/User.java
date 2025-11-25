package com.fiveguys.RIA.RIA_Backend.user.model.entity;

import com.fiveguys.RIA.RIA_Backend.auth.service.entity.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "USER",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_USER_EMAIL",
            columnNames = "EMAIL"
        )
    }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROLE_ID", nullable = false)
  private Role role;

  @Column(name = "EMPLOYEE_NO", nullable = false, length = 50, unique = true)
  private String employeeNo;

  @Column(name = "NAME", nullable = false, length = 50)
  private String name;

  @Column(name = "EMAIL", nullable = false, length = 255)
  private String email;

  @Column(name = "PASSWORD", nullable = false, length = 255)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "DEPARTMENT", length = 50)
  private Department department;

  @Column(name = "POSITION", length = 100)
  private String position;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private Status status;

  @Column(name = "IS_DELETED", nullable = false)
  private boolean isDeleted;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT", nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.isDeleted = false;
    if (this.status == null) {
      this.status = Status.TEMP_PASSWORD;
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public enum Status {
    ACTIVE,
    INACTIVE,
    TEMP_PASSWORD
  }

  public enum Department {
    SALES,
    ADMIN
  }

  public void changePassword(String encodedPassword) {
    this.password = encodedPassword;
  }

  public void activateAccount() {
    if (this.status == Status.TEMP_PASSWORD) {
      this.status = Status.ACTIVE;
    }
  }

  public void changeRoleAndPosition(Role role, String position) {
    this.role = role;
    this.position = position;
  }

  public void softDelete() {
    this.isDeleted = true;
    this.status = Status.INACTIVE;
  }

}
