package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueDetailResponseDto.StoreInfo;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.LatestSettlementProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueDetailProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueListProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.SettlementAggProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.StoreInfoProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RevenueLoader {

  private final RevenueRepository revenueRepository;

  public Page<RevenueListProjection> loadRevenueList(String storeType, Long creatorId, String keyword, Pageable pageable
  ) {
    return revenueRepository.findRevenueList(storeType, creatorId, keyword, pageable);
  }

  public RevenueDetailProjection loadRevenueDetail(Long revenueId) {
    return revenueRepository.findRevenueDetail(revenueId);
  }

  public List<StoreInfoProjection> loadStoreInfosByContract(Long contractId) {
    return revenueRepository.findStoreInfosByContract(contractId);
  }

  public SettlementAggProjection loadSettlementAgg(Long revenueId) {
    return revenueRepository.findSettlementAgg(revenueId);
  }

  public LatestSettlementProjection loadLatestSettlement(Long revenueId) {
    return revenueRepository.findLatestSettlement(revenueId);
  }

  public List<RevenueSettlement> loadSettlementHistoryByContractAndYearMonthBetween(
      Long contractId,
      int startYm,
      int endYm
  ) {
    return revenueRepository.findHistoryByContractAndYearMonthBetween(
        contractId,
        startYm,
        endYm
    );
  }
}
