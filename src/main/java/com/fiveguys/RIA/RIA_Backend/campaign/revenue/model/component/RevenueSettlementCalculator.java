package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueSettlementRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.DailySettlementRow;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.MonthlySettlementRow;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ContractErrorCode;
import java.math.BigDecimal;
import java.time.LocalDate;
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

  @Transactional
  public void settleMonth(int year, int month, List<MonthlySettlementRow> rows) {

    long before = revenueSettlementRepository
        .countBySettlementYearAndSettlementMonth(year, month);
    System.out.println("[SETTLE-MONTH] year=" + year + ", month=" + month
        + " BEFORE=" + before + " rows.size=" + rows.size());

    revenueSettlementRepository.deleteBySettlementYearAndSettlementMonth(year, month);

    Map<Long, BigDecimal> contractDeltaMap = new HashMap<>();
    YearMonth ym = YearMonth.of(year, month);

    for (MonthlySettlementRow row : rows) {
      Long tenantId   = row.getStoreTenantMapId();
      Long contractId = row.getContractId();
      Long projectId  = row.getProjectId();

      BigDecimal totalSales = nvl(row.getTotalSalesAmount());

      Contract contract = contractRepository.findById(contractId)
          .orElseThrow(() -> new CustomException(ContractErrorCode.CONTRACT_NOT_FOUND));

      BigDecimal baseRent = contract.calcBaseRent(ym);

      BigDecimal commissionRate = nvl(contract.getCommissionRate());
      BigDecimal commissionAmount =
          totalSales.multiply(commissionRate)
              .divide(BigDecimal.valueOf(100));

      BigDecimal finalRevenue = baseRent.add(commissionAmount);

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

      contractDeltaMap.merge(contractId, finalRevenue, BigDecimal::add);
    }

    long after = revenueSettlementRepository
        .countBySettlementYearAndSettlementMonth(year, month);
    System.out.println("[SETTLE-MONTH] year=" + year + ", month=" + month
        + " AFTER=" + after);

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

  @Transactional
  public void settleDay(LocalDate date, List<DailySettlementRow> rows) {
    int year = date.getYear();
    int month = date.getMonthValue();
    int day = date.getDayOfMonth();

    // 해당 일자 정산 전체 삭제 → idempotent
    revenueSettlementRepository
        .deleteBySettlementYearAndSettlementMonthAndSettlementDay(year, month, day);

    Map<Long, BigDecimal> contractDeltaMap = new HashMap<>();

    for (DailySettlementRow row : rows) {
      Long storeTenantMapId = row.getStoreTenantMapId();
      Long contractId       = row.getContractId();
      Long projectId        = row.getProjectId();

      BigDecimal totalSales = nvl(row.getTotalSalesAmount());

      BigDecimal commissionRate = loadCommissionRate(contractId);

      BigDecimal commissionAmount = totalSales
          .multiply(commissionRate)
          .divide(BigDecimal.valueOf(100));

      // 팝업/전시: 임대료 없이 수수료만 수익으로 본다
      BigDecimal finalRevenue = commissionAmount;

      upsertDailySettlement(
          storeTenantMapId,
          contractId,
          projectId,
          year,
          month,
          day,
          totalSales,
          commissionRate,
          commissionAmount,
          finalRevenue
      );

      contractDeltaMap.merge(contractId, finalRevenue, BigDecimal::add);
    }

    // 계약별 REVENUE.TOTAL_PRICE 재계산
    for (Long contractId : contractDeltaMap.keySet()) {
      BigDecimal totalPrice =
          revenueSettlementRepository.sumFinalRevenueByContractId(contractId);

      Revenue revenue = revenueRepository.findByContract_ContractId(contractId)
          .orElseThrow();

      revenue.updateTotalPrice(totalPrice);
    }
  }

  private BigDecimal loadCommissionRate(Long contractId) {
    Contract contract = contractRepository.findById(contractId)
        .orElseThrow(() -> new CustomException(ContractErrorCode.CONTRACT_NOT_FOUND));

    return nvl(contract.getCommissionRate());
  }

  private void upsertDailySettlement(
      Long storeTenantMapId,
      Long contractId,
      Long projectId,
      int year,
      int month,
      int day,
      BigDecimal totalSales,
      BigDecimal commissionRate,
      BigDecimal commissionAmount,
      BigDecimal finalRevenue
  ) {
    RevenueSettlement settlement =
        revenueSettlementRepository
            .findByStoreTenantMapIdAndSettlementYearAndSettlementMonthAndSettlementDay(
                storeTenantMapId, year, month, day
            )
            .orElseGet(() -> RevenueSettlement.builder()
                .storeTenantMapId(storeTenantMapId)
                .contractId(contractId)
                .projectId(projectId)
                .settlementYear(year)
                .settlementMonth(month)
                .settlementDay(day)
                .build()
            );

    settlement.applySettlement(
        totalSales,
        commissionRate,
        commissionAmount,
        finalRevenue
    );

    revenueSettlementRepository.save(settlement);
  }
}
