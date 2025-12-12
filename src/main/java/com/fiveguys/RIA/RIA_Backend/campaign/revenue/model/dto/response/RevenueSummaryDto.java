package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RevenueSummaryDto {

  private final Long revenueId;
  private final Long baseRentSnapshot;
  private final String remark;
  private final Revenue.Status status;
  private final BigDecimal totalPrice;
  private final LocalDateTime createdAt;
}
