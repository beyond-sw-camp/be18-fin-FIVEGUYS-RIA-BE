package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.MonthlySettlementRow;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RevenueSettlementRepository extends JpaRepository<RevenueSettlement, Long> {

  // 특정 연/월 정산 기록 전체 삭제 (idempotent 재정산용)
  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("delete from RevenueSettlement rs where rs.settlementYear = :year and rs.settlementMonth = :month")
  void deleteBySettlementYearAndSettlementMonth(@Param("year") int year, @Param("month") int month);
  // 계약별 누적 정산 금액 합계
  @Query("""
      select coalesce(sum(r.finalRevenue), 0)
      from RevenueSettlement r
      where r.contractId = :contractId
      """)
  BigDecimal sumFinalRevenueByContractId(@Param("contractId") Long contractId);

  // 특정 연·월의 매출 집계를 계약 기간과 매장-계약 매핑에 따라 정확히 필터링하여,
  // 정산 대상 계약별 월매출 원천 데이터를 추출하기 위해 사용한다.
  @Query(value = """
    SELECT
        sm.STORE_TENANT_MAP_ID  AS storeTenantMapId,
        c.CONTRACT_ID           AS contractId,
        c.PROJECT_ID            AS projectId,
        sm.TOTAL_SALES_AMOUNT   AS totalSalesAmount
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
      AND c.CONTRACT_START_DATE <= :endOfMonth
      AND c.CONTRACT_END_DATE   >= :startOfMonth
    """, nativeQuery = true)
  List<MonthlySettlementRow> findMonthlySettlementRows(
      @Param("year") int year,
      @Param("month") int month,
      @Param("startOfMonth") LocalDate startOfMonth,
      @Param("endOfMonth") LocalDate endOfMonth
  );
}