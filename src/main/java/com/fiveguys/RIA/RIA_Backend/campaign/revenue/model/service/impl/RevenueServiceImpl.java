package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.RevenueLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.RevenueMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueListItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenuePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueListProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

  private final RevenueLoader revenueLoader;
  private final RevenueMapper revenueMapper;

  @Override
  public RevenuePageResponseDto<RevenueListItemResponseDto> getRevenueList(
      String storeType,
      Long creatorId,
      Pageable pageable
  ) {

    Page<RevenueListProjection> page =
        revenueLoader.loadRevenueList(storeType, creatorId, pageable);

    Page<RevenueListItemResponseDto> mapped =
        page.map(revenueMapper::toRevenueListItemResponseDto);

    return RevenuePageResponseDto.<RevenueListItemResponseDto>builder()
        .page(mapped.getNumber())
        .size(mapped.getSize())
        .totalCount(mapped.getTotalElements())
        .data(mapped.getContent())
        .build();
  }
}