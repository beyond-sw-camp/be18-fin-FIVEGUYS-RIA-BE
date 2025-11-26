package com.fiveguys.RIA.RIA_Backend.user.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyProjectResponseDto {
  private Long projectId;
  private String title;
  private String clientCompanyName;
  private LocalDate startDay;
  private LocalDate endDay;
  private String type;
  private String salesManagerName;
  private String status;
}
