package com.fiveguys.RIA.RIA_Backend.user.model.entity;


import com.fiveguys.RIA.RIA_Backend.common.model.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ROLE_ID", nullable = false)
  private Role role;

  @Column(name = "EMPLOYEE_NO", nullable = false, length = 50, unique = true)
  private String employeeNo;


  @Column(name = "NAME", length = 10)
  private String name;

  @Column(name = "EMAIL", length = 255)
  private String email;

  @Column(name = "PASSWORD", nullable = false, length = 255)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "DEPARTMENT", length = 50)
  private Department department;

  @Column(name = "POSITION", length = 100)
  private String position;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20)
  private Status status; // 계정 상태 (ACTIVE, INACTIVE(퇴사) 등)

  @Column(name = "IS_DELETED", nullable = false)
  private boolean isDeleted;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    if (this.status == null) {
      this.status = Status.TEMP_PASSWORD;
    }
  }

  public enum Status {
    ACTIVE,
    INACTIVE,
    TEMP_PASSWORD
  }

  public enum Department {
    PLAN,
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

}

