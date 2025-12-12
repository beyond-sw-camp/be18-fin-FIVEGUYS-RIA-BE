package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectUpdateRequestDto {

  private String title;                // 프로젝트명
  private String description;          // 설명
  private String type;                 // EXHIBITION / RENTAL / POPUP
  private BigDecimal expectedRevenue;     // 예상매출
  private BigDecimal expectedMarginRate; // 예상이익률 (%)
  private LocalDate startDay;          // 시작일
  private LocalDate endDay;            // 종료일

  /**
   * 요청 DTO를 Project 엔티티가 이해할 수 있는 형식으로 변환
   * (Controller/Service에서 공통적으로 호출)
   */
  public Project.Type toProjectType() {
    if (this.type == null || this.type.isBlank()) {
      return null;
    }
    try {
      return Project.Type.valueOf(this.type.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null; // 잘못된 문자열이면 무시
    }
  }
}
