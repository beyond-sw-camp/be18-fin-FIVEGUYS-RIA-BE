package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueListProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RevenueLoader {

  private final RevenueRepository revenueRepository;

  public Page<RevenueListProjection> loadRevenueList(String storeType, Long creatorId, Pageable pageable) {
    return revenueRepository.findRevenueList(storeType, creatorId, pageable);
  }
}
