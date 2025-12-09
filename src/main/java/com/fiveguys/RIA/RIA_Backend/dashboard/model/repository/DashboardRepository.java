package com.fiveguys.RIA.RIA_Backend.dashboard.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.BrandMonthlyAmountProjection;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.FloorMonthlySalesProjection;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.MonthlySettlementTrendProjection;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.repository.projection.StoreAreaEfficiencyProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<RevenueSettlement, Long> {

  boolean existsBySettlementYear(int year);



  @Query(value = """
      SELECT
          COALESCE(stm.STORE_DISPLAY_NAME, s.STORE_NUMBER) AS storeName,
          f.FLOOR_NAME                                     AS floorName,
          SUM(sm.TOTAL_SALES_AMOUNT)                       AS totalAmount
      FROM SALES_MONTHLY sm
          JOIN STORE_TENANT_MAP stm
              ON sm.STORE_TENANT_MAP_ID = stm.STORE_TENANT_MAP_ID
          JOIN STORE s
              ON stm.STORE_ID = s.STORE_ID
          LEFT JOIN FLOOR f
              ON s.FLOOR_ID = f.FLOOR_ID
      WHERE sm.SALES_YEAR = :year
        AND sm.SALES_MONTH = :month
      GROUP BY
          COALESCE(stm.STORE_DISPLAY_NAME, s.STORE_NUMBER),
          f.FLOOR_NAME
      """,
      nativeQuery = true)
  List<BrandMonthlyAmountProjection> findBrandMonthlyAmount(
      @Param("year") int year,
      @Param("month") int month
  );

  @Query(value = """
    SELECT
        rs.settlement_month AS month,
        SUM(rs.final_revenue) AS totalAmount
    FROM revenue_settlement rs
    WHERE rs.settlement_year = :year
    GROUP BY rs.settlement_month
    ORDER BY rs.settlement_month
    """, nativeQuery = true)
  List<MonthlySettlementTrendProjection> findMonthlySettlementTrend(
      @Param("year") int year
  );

  @Query(value = """
      SELECT
          stm.STORE_TENANT_MAP_ID                               AS storeTenantMapId,
          COALESCE(stm.STORE_DISPLAY_NAME, s.STORE_NUMBER)      AS storeName,
          f.FLOOR_NAME                                          AS floorName,
          s.AREA_SIZE                                           AS areaSize,
          SUM(rs.FINAL_REVENUE)                                 AS finalRevenue
      FROM REVENUE_SETTLEMENT rs
          JOIN STORE_TENANT_MAP stm
              ON rs.STORE_TENANT_MAP_ID = stm.STORE_TENANT_MAP_ID
          JOIN STORE s
              ON stm.STORE_ID = s.STORE_ID
          LEFT JOIN FLOOR f
              ON s.FLOOR_ID = f.FLOOR_ID
      WHERE rs.SETTLEMENT_YEAR  = :year
        AND rs.SETTLEMENT_MONTH = :month
        AND stm.STORE_TYPE      = 'REGULAR'     -- 상설 매장만 (월 정산)
        AND rs.SETTLEMENT_DAY IS NULL           -- 월 정산 레코드만
      GROUP BY
          stm.STORE_TENANT_MAP_ID,
          COALESCE(stm.STORE_DISPLAY_NAME, s.STORE_NUMBER),
          f.FLOOR_NAME,
          s.AREA_SIZE
      """,
      nativeQuery = true)
  List<StoreAreaEfficiencyProjection> findStoreAreaEfficiency(
      @Param("year") int year,
      @Param("month") int month
  );

  @Query(value = """
      SELECT
          f.FLOOR_NAME              AS floorName,
          SUM(sm.TOTAL_SALES_AMOUNT) AS totalAmount
      FROM SALES_MONTHLY sm
          JOIN STORE_TENANT_MAP stm
              ON sm.STORE_TENANT_MAP_ID = stm.STORE_TENANT_MAP_ID
          JOIN STORE s
              ON stm.STORE_ID = s.STORE_ID
          LEFT JOIN FLOOR f
              ON s.FLOOR_ID = f.FLOOR_ID
      WHERE sm.SALES_YEAR  = :year
        AND sm.SALES_MONTH = :month
      GROUP BY f.FLOOR_NAME
      ORDER BY f.FLOOR_NAME
      """,
      nativeQuery = true)
  List<FloorMonthlySalesProjection> findFloorMonthlySales(
      @Param("year") int year,
      @Param("month") int month
  );
}
