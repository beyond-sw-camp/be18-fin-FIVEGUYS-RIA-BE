package com.fiveguys.RIA.RIA_Backend.user.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MyProjectResponseDto {
  private Long projectId;
  private String title;
  private String clientCompanyName;
  private LocalDate startDay;
  private LocalDate endDay;
  private Integer expectedRevenue;
  private Double expectedMarginRate;
  private Integer expectedProfit;
  private String salesManagerName;
  private String status;
}
