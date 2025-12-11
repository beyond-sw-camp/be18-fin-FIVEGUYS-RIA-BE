// RevenueServiceImpl.java
package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.RevenueLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.RevenueMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.SalesLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.SalesMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.SalesValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.DailySalesHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueListItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenuePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueSettlementHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.LatestSettlementProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueDetailProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueListProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.SettlementAggProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.StoreInfoProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.RevenueService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

  private final RevenueLoader revenueLoader;
  private final RevenueMapper revenueMapper;
  private final SalesLoader salesLoader;
  private final SalesMapper salesMapper;
  private final SalesValidator salesValidator;

  @Override
  public RevenuePageResponseDto<RevenueListItemResponseDto> getRevenueList(
      String storeType,
      Long creatorId,
      Pageable pageable
  ) {
    Page<RevenueListProjection> page =
        revenueLoader.loadRevenueList(storeType, creatorId, pageable);

    log.info("projections: {}", page.getContent());

    Page<RevenueListItemResponseDto> mapped =
        page.map(revenueMapper::toRevenueListItemResponseDto);

    return RevenuePageResponseDto.<RevenueListItemResponseDto>builder()
        .page(mapped.getNumber())
        .size(mapped.getSize())
        .totalCount(mapped.getTotalElements())
        .data(mapped.getContent())
        .build();
  }

  @Override
  public RevenueDetailResponseDto getRevenueDetail(Long revenueId, Long storeTenantMapId) {

    RevenueDetailProjection p =
        revenueLoader.loadRevenueDetail(revenueId);

    List<StoreInfoProjection> stores =
        revenueLoader.loadStoreInfosByContract(p.getContractId());

    SettlementAggProjection agg =
        revenueLoader.loadSettlementAgg(revenueId);

    LatestSettlementProjection latest =
        revenueLoader.loadLatestSettlement(revenueId);

    return revenueMapper.toRevenueDetailResponseDto(p, stores, agg, latest);
  }

  @Override
  public RevenueSettlementHistoryResponseDto getSettlementHistoryByContractAndPeriod(
      Long contractId,
      int startYear,
      int startMonth,
      int endYear,
      int endMonth
  ) {
    // 1) 입력 검증
    salesValidator.validateYearMonthRange(startYear, startMonth, endYear, endMonth);

    int startYm = startYear * 100 + startMonth;
    int endYm = endYear * 100 + endMonth;

    // 2) 데이터 로드
    List<RevenueSettlement> settlements =
        revenueLoader.loadSettlementHistoryByContractAndYearMonthBetween(
            contractId,
            startYm,
            endYm
        );

    // 3) 결과 검증
    salesValidator.validateSettlementResult(settlements);

    // 4) 매핑
    return revenueMapper.toRevenueSettlementHistoryResponseDto(
        contractId,
        startYear,
        startMonth,
        endYear,
        endMonth,
        settlements
    );
  }

  @Override
  public DailySalesHistoryResponseDto getDailySalesHistory(
      Long storeTenantMapId,
      LocalDate startDate,
      LocalDate endDate
  ) {
    // 1) 입력 검증
    salesValidator.validateDateRange(startDate, endDate);

    // 2) 데이터 로드
    List<SalesDaily> dailyList =
        salesLoader.loadDailySalesByStoreAndDateRange(
            storeTenantMapId,
            startDate,
            endDate
        );

    // 3) 결과 검증
    salesValidator.validateDailySalesResult(dailyList);

    // 4) 매핑
    return salesMapper.toDailySalesHistoryResponseDto(
        storeTenantMapId,
        startDate,
        endDate,
        dailyList
    );
  }
}
