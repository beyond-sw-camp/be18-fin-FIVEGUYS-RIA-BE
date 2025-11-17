package com.fiveguys.RIA.RIA_Backend.facility.zone.model.entity;

import com.fiveguys.RIA.RIA_Backend.facility.building.model.entity.Building;
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
import jakarta.persistence.PreUpdate;
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
    name = "ZONE",
    uniqueConstraints = @UniqueConstraint(columnNames = {"building_id", "zoneName"})
)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Zone {

  public enum Category {
    EAST, WEST, UNIFIED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long zoneId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "building_id", nullable = false)
  private Building building;

  @Column(nullable = false, length = 50)
  private String zoneName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Category category;

  @Column(length = 255)
  private String description;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
