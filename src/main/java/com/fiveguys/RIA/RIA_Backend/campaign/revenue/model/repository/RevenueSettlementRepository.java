package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.DailySettlementRow;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.MonthlySettlementRow;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap.StoreType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RevenueSettlementRepository extends JpaRepository<RevenueSettlement, Long> {


  long countBySettlementYearAndSettlementMonth(int settlementYear, int settlementMonth);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("""
      delete from RevenueSettlement rs
      where rs.settlementYear = :year
        and rs.settlementMonth = :month
      """)
  void deleteBySettlementYearAndSettlementMonth(@Param("year") int year,
      @Param("month") int month);

  // 일 재정산용
  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("""
      delete from RevenueSettlement rs
      where rs.settlementYear = :year
        and rs.settlementMonth = :month
        and rs.settlementDay = :day
      """)
  void deleteBySettlementYearAndSettlementMonthAndSettlementDay(@Param("year") int year,
      @Param("month") int month,
      @Param("day") int day);

  Optional<RevenueSettlement>
  findByStoreTenantMapIdAndSettlementYearAndSettlementMonthAndSettlementDay(
      Long storeTenantMapId,
      int settlementYear,
      int settlementMonth,
      Integer settlementDay
  );

  @Query("""
      select coalesce(sum(r.finalRevenue), 0)
      from RevenueSettlement r
      where r.contractId = :contractId
      """)
  BigDecimal sumFinalRevenueByContractId(@Param("contractId") Long contractId);

  @Query(value = """
    SELECT
        sm.STORE_TENANT_MAP_ID  AS storeTenantMapId,
        c.CONTRACT_ID           AS contractId,
        c.PROJECT_ID            AS projectId,
        sm.TOTAL_SALES_AMOUNT   AS totalSalesAmount
    FROM SALES_MONTHLY sm
        JOIN STORE_TENANT_MAP stm
            ON sm.STORE_TENANT_MAP_ID = stm.STORE_TENANT_MAP_ID
        JOIN STORE s
            ON stm.STORE_ID = s.STORE_ID
        JOIN CONTRACT c
            ON stm.CONTRACT_ID = c.CONTRACT_ID
        JOIN STORE_CONTRACT_MAP scm
            ON scm.CONTRACT_ID = c.CONTRACT_ID
           AND scm.STORE_ID      = stm.STORE_ID
    WHERE sm.SALES_YEAR  = :year
      AND sm.SALES_MONTH = :month
      AND c.CONTRACT_START_DATE <= :endOfMonth
      AND c.CONTRACT_END_DATE   >= :startOfMonth
      AND s.TYPE = :storeType
    """, nativeQuery = true)
  List<MonthlySettlementRow> findMonthlySettlementRowsByStoreType(
      @Param("year") int year,
      @Param("month") int month,
      @Param("startOfMonth") LocalDate startOfMonth,
      @Param("endOfMonth") LocalDate endOfMonth,
      @Param("storeType") String storeType
  );


  @Query(value = """
    SELECT
        sd.STORE_TENANT_MAP_ID    AS storeTenantMapId,
        c.CONTRACT_ID             AS contractId,
        c.PROJECT_ID              AS projectId,
        sd.SALES_DATE             AS salesDate,
        sd.TOTAL_SALES_AMOUNT     AS totalSalesAmount,
        sd.VIP_SALES_AMOUNT       AS vipSalesAmount,
        sd.TOTAL_SALES_COUNT      AS totalSalesCount,
        sd.VIP_SALES_COUNT        AS vipSalesCount
    FROM SALES_DAILY sd
        JOIN STORE_TENANT_MAP stm
            ON sd.STORE_TENANT_MAP_ID = stm.STORE_TENANT_MAP_ID
        JOIN STORE s
            ON stm.STORE_ID = s.STORE_ID
        JOIN CONTRACT c
            ON stm.CONTRACT_ID = c.CONTRACT_ID
    WHERE sd.SALES_DATE = :targetDate
      AND stm.STATUS = 'ACTIVE'
      AND s.TYPE IN (:storeTypes)
      AND c.CONTRACT_START_DATE <= :targetDate
      AND c.CONTRACT_END_DATE   >= :targetDate
    """, nativeQuery = true)
  List<DailySettlementRow> findDailySettlementRowsByStoreTypes(
      @Param("targetDate") LocalDate targetDate,
      @Param("storeTypes") List<StoreType> storeTypes
  );

}
