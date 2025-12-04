package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueSettlementRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.MonthlySettlementRow;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ContractErrorCode;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RevenueSettlementCalculator {

  private final RevenueSettlementRepository revenueSettlementRepository;
  private final RevenueRepository revenueRepository;
  private final ContractRepository contractRepository;

  /**
   * 역할:
   * - 특정 연/월의 매출 집계를 계약 조건에 따라 해석하여
   *   REVENUE_SETTLEMENT를 생성/갱신하고,
   *   REVENUE.TOTAL_PRICE를 최신 값으로 맞춘다.
   * - "정산 계산" 책임만 가진다 (스케줄링/날짜결정은 Service/스케줄러에서 담당).
   */
  @Transactional
  public void settleMonth(int year, int month, List<MonthlySettlementRow> rows) {

    // 1. 해당 연/월 기존 정산 내역 제거 → idempotent 재정산
    revenueSettlementRepository.deleteBySettlementYearAndSettlementMonth(year, month);

    // 2. 계약별 누적 정산 금액 계산용 맵
    Map<Long, BigDecimal> contractDeltaMap = new HashMap<>();

    YearMonth ym = YearMonth.of(year, month);

    for (MonthlySettlementRow row : rows) {
      Long tenantId   = row.getStoreTenantMapId();
      Long contractId = row.getContractId();
      Long projectId  = row.getProjectId();

      BigDecimal totalSales = nvl(row.getTotalSalesAmount());

      // 2-0. 계약 로드
      Contract contract = contractRepository.findById(contractId)
          .orElseThrow(() -> new CustomException(ContractErrorCode.CONTRACT_NOT_FOUND));

      // 2-1. 임대료 계산 (MONTHLY / YEARLY / FIXED)
      BigDecimal baseRent = contract.calcBaseRent(ym);

      // 2-2. 수수료율/수수료 계산 (계약서 기준 수수료율 사용)
      BigDecimal commissionRate = nvl(contract.getCommissionRate()); // % 단위
      BigDecimal commissionAmount =
          totalSales.multiply(commissionRate)
              .divide(BigDecimal.valueOf(100));

      // 2-3. 최종 수익
      BigDecimal finalRevenue = baseRent.add(commissionAmount);

      // 정산 row 생성
      RevenueSettlement settlement = RevenueSettlement.builder()
          .storeTenantMapId(tenantId)
          .contractId(contractId)
          .projectId(projectId)
          .settlementYear(year)
          .settlementMonth(month)
          .totalSalesAmount(totalSales)
          .commissionRate(commissionRate)
          .commissionAmount(commissionAmount)
          .finalRevenue(finalRevenue)
          .build();

      revenueSettlementRepository.save(settlement);

      // 계약별 누적 (해당 월 정산액 합)
      contractDeltaMap.merge(contractId, finalRevenue, BigDecimal::add);
    }

    // 3. REVENUE 누적값 재계산
    for (Long contractId : contractDeltaMap.keySet()) {

      BigDecimal totalPrice =
          revenueSettlementRepository.sumFinalRevenueByContractId(contractId);

      Revenue revenue = revenueRepository.findByContract_ContractId(contractId)
          .orElseThrow();

      revenue.updateTotalPrice(totalPrice);
    }
  }

  private BigDecimal nvl(BigDecimal v) {
    return v == null ? BigDecimal.ZERO : v;
  }
}
