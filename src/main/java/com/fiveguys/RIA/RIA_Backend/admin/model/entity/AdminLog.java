package com.fiveguys.RIA.RIA_Backend.admin.model.entity;

import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ADMIN_LOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_LOG_ID")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ID")
    private User admin;

    @Column(name = "LOG_NAME", nullable = false, length = 255)
    private String logName;

    @Column(name = "USER_NAME", nullable = false ,length = 50)
    private String userName;

    @Column(name = "EMPLOYEE_NO", length = 50)
    private String employeeNo;

    @Column(name = "RESOURCE", nullable = false, length = 255)
    private String resource;

    @Column(name ="STATE", length = 20)
    private String state;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
