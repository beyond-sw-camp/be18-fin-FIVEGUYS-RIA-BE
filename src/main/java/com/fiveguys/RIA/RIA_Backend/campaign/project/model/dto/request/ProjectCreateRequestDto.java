package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectCreateRequestDto {

  private Long salesManagerId;     // 영업 담당자 (USER)
  private Long clientCompanyId;    // 고객사
  private Long clientId;           // 담당자
  private String title;            // 프로젝트명
  private String type;
  private String description;
  private LocalDate startDay;
  private LocalDate endDay;
  private BigDecimal expectedRevenue;
  private BigDecimal expectedMarginRate;

  public Project.Type toProjectType() {
    try {
      return Project.Type.valueOf(this.type.toUpperCase());
    } catch (Exception e) {
      throw new CustomException(ProjectErrorCode.INVALID_PROJECT_TYPE);
    }
  }
}
