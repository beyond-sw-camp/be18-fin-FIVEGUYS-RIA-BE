package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueListItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenuePageResponseDto;
import org.springframework.data.domain.Pageable;

public interface RevenueService {

  RevenuePageResponseDto<RevenueListItemResponseDto> getRevenueList(
      String storeType,
      Long creatorId,
      Pageable pageable
  );
}