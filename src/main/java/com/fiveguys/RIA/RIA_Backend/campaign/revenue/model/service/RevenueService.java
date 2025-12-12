package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.DailySalesHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueListItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenuePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueSettlementHistoryResponseDto;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

public interface RevenueService {

  RevenuePageResponseDto<RevenueListItemResponseDto> getRevenueList(
      String storeType,
      Long creatorId,
      String keyword,
      Pageable pageable
  );

  RevenueDetailResponseDto getRevenueDetail(Long revenueId, Long storeTenantMapId);

  RevenueSettlementHistoryResponseDto getSettlementHistoryByContractAndPeriod(
      Long contractId,
      int startYear,
      int startMonth,
      int endYear,
      int endMonth
  );

  DailySalesHistoryResponseDto getDailySalesHistory(
      Long storeTenantMapId,
      LocalDate startDate,
      LocalDate endDate
  );


}