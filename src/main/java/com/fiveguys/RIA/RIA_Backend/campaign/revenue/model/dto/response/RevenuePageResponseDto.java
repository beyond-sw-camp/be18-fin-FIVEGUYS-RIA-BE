package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RevenuePageResponseDto<T> {
  private int page;
  private int size;
  private long totalCount;
  private List<T> data;
}