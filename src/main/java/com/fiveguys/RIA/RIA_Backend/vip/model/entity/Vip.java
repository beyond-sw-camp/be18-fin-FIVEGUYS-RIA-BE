package com.fiveguys.RIA.RIA_Backend.vip.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "VIP")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Vip {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "VIP_ID")
  private Long id;

  @Column(name = "CUSTOMER_ID", nullable = false, unique = true)
  private Long customerId;

  @Column(name = "NAME", nullable = false, length = 20)
  private String name;

  @Column(name = "PHONE", nullable = false, length = 20)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(name = "GRADE", nullable = false)
  private VipGrade grade;

  @CreationTimestamp
  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // 생성용 팩토리 메서드인데 우리는 그냥 db에 직접 insert할꺼라 안쓸것 같기도?
  public static Vip create(Long customerId, String name, String phone, VipGrade grade) {
    return Vip.builder()
        .customerId(customerId)
        .name(name)
        .phone(phone)
        .grade(grade)
        .build();
  }

  // 등급 변경 등 도메인 메서드 필요하면 아래에 추가
  public void changeGrade(VipGrade grade) {
    this.grade = grade;
  }

  public enum VipGrade {

    PSR_BLACK,          // 최상위 0.1%, 구매일수 연 6일 이상
    PSR_WHITE,          // 1억 2천만 원 이상

    PARK_JADE_BLACK,    // 7천만 원 이상
    PARK_JADE_WHITE,    // 5천만 원 이상
    PARK_JADE_BLUE,     // 3천만 원 이상

    JADE_PLUS,          // 1천만 원 이상
    JADE                // 5백만 원 이상 또는 3개월간 3백만 원
  }

}


