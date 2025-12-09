package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailySalesHistoryResponseDto {

  private Long storeTenantMapId;              // 매장 테넌트 매핑 ID
  private LocalDate startDate;                // 조회 시작일
  private LocalDate endDate;                  // 조회 종료일
  private List<DailySalesItemResponseDto> items; // 일별 매출 목록
}