package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.MonthlySettlementRow;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RevenueSettlementRepository extends JpaRepository<RevenueSettlement, Long> {

  // 특정 연/월 정산 기록 전체 삭제 (idempotent 재정산용)
  void deleteBySettlementYearAndSettlementMonth(int settlementYear, int settlementMonth);

  // 계약별 누적 정산 금액 합계
  @Query("""
      select coalesce(sum(r.finalRevenue), 0)
      from RevenueSettlement r
      where r.contractId = :contractId
      """)
  BigDecimal sumFinalRevenueByContractId(@Param("contractId") Long contractId);

  // 월 정산에 사용할 원천 데이터 조회 (월매출 + 계약/임대료/수수료)
  @Query(value = """
        SELECT
            sm.STORE_TENANT_MAP_ID        AS storeTenantMapId,
            c.CONTRACT_ID                 AS contractId,
            c.PROJECT_ID                  AS projectId,
            sm.TOTAL_SALES_AMOUNT         AS totalSalesAmount,
            scm.RENT_PRICE                AS rentPrice,
            c.COMMISSION_RATE             AS commissionRate
        FROM SALES_MONTHLY sm
          JOIN STORE_TENANT_MAP stm
            ON sm.STORE_TENANT_MAP_ID = stm.STORE_TENANT_MAP_ID
          JOIN CONTRACT c
            ON stm.CONTRACT_ID = c.CONTRACT_ID
          JOIN STORE_CONTRACT_MAP scm
            ON scm.CONTRACT_ID = c.CONTRACT_ID
           AND scm.STORE_ID      = stm.STORE_ID
        WHERE sm.SALES_YEAR  = :year
          AND sm.SALES_MONTH = :month
        """, nativeQuery = true)
  List<MonthlySettlementRow> findMonthlySettlementRows(
      @Param("year") int year,
      @Param("month") int month
  );
}