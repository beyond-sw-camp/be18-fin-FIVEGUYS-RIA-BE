package com.fiveguys.RIA.RIA_Backend.dashboard.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.MonthlyManagerSalesRowProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<RevenueSettlement, Long> {

  /**
   * 대시보드용: 담당자 기준 월별 매출 합계
   *  - 기준: PROJECT.SALES_MANAGER_ID
   *  - 구간: [startYm, endYm] (yyyymm)
   */
  @Query(value = """
        SELECT
            sm.SALES_YEAR                AS salesYear,
            sm.SALES_MONTH               AS salesMonth,
            SUM(sm.TOTAL_SALES_AMOUNT)   AS totalSalesAmount
        FROM SALES_MONTHLY sm
            JOIN STORE_TENANT_MAP stm
                ON sm.STORE_TENANT_MAP_ID = stm.STORE_TENANT_MAP_ID
            JOIN CONTRACT c
                ON stm.CONTRACT_ID = c.CONTRACT_ID
            JOIN PROJECT p
                ON p.PROJECT_ID = c.PROJECT_ID
        WHERE ( :managerId IS NULL OR p.SALES_MANAGER_ID = :managerId )
          AND (sm.SALES_YEAR * 100 + sm.SALES_MONTH) BETWEEN :startYm AND :endYm
        GROUP BY sm.SALES_YEAR, sm.SALES_MONTH
        ORDER BY sm.SALES_YEAR, sm.SALES_MONTH
        """,
      nativeQuery = true)
  List<MonthlyManagerSalesRowProjection> findMonthlyManagerSalesByManager(
      @Param("managerId") Long managerId,
      @Param("startYm") int startYm,
      @Param("endYm") int endYm
  );
}