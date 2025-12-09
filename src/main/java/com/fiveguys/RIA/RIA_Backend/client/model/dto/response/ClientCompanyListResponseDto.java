package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.time.LocalDate;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCompanyListResponseDto {
  private Long clientCompanyId;
  private String companyName;
  private String category;
  private LocalDateTime createdAt;

  // 카드 하단 정보
  private String managerName;            // 담당 영업 (Project.salesManager.name)
  private LocalDate contractStartDay;    // 최신 계약 시작일
  private LocalDate contractEndDay;      // 최신 계약 종료일
  private String projectStatus;
}
