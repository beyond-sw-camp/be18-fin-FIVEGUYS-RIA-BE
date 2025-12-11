package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {

  Optional<Revenue> findByContract_ContractId(Long contractId);

  // ============================
  // 목록 조회 (최신 정산 1건만 조인)
  // ============================
  @Query(
      value = """
        SELECT
            r.revenue_id              AS revenueId,
            p.project_id              AS projectId,
            c.contract_id             AS contractId,
            rs.revenue_settlement_id  AS settlementId,

            c.contract_title          AS contractTitle,
            cc.company_name           AS clientCompanyName,

            :storeType                AS storeType,   -- 파라미터 그대로 반환

            u.user_id                 AS managerId,
            u.name                    AS managerName,

            rs.settlement_year        AS settlementYear,
            rs.settlement_month       AS settlementMonth,
            rs.final_revenue          AS finalRevenue,

            c.contract_start_date     AS contractStartDay,
            c.contract_end_date       AS contractEndDay
        FROM revenue r
        JOIN project p
            ON p.project_id = r.project_id
        JOIN contract c
            ON c.contract_id = r.contract_id
        JOIN client_company cc
            ON cc.client_company_id = r.client_company_id
        JOIN user u
            ON u.user_id = r.created_user

        -- 계약별 최신 정산 1건만 조인
        LEFT JOIN (
            SELECT *
            FROM (
                SELECT
                    rs.*,
                    ROW_NUMBER() OVER (
                        PARTITION BY rs.contract_id
                        ORDER BY
                            rs.settlement_year DESC,
                            rs.settlement_month DESC,
                            rs.revenue_settlement_id DESC
                    ) AS rn
                FROM revenue_settlement  rs
            ) t
            WHERE t.rn = 1
        ) rs
            ON rs.contract_id = c.contract_id

        WHERE (:creatorId IS NULL OR r.created_user = :creatorId)
          AND (
              :storeType IS NULL
              OR EXISTS (
                    SELECT 1
                    FROM store_contract_map stm2
                    JOIN store s2
                      ON s2.store_id = stm2.store_id
                   WHERE stm2.contract_id = c.contract_id
                     AND s2.type = :storeType
              )
          )

        ORDER BY COALESCE(rs.settlement_year, 0) DESC,
                 COALESCE(rs.settlement_month, 0) DESC,
                 r.revenue_id DESC
        """,
      countQuery = """
        SELECT COUNT(*)
        FROM revenue r
        """,
      nativeQuery = true
  )
  Page<RevenueListProjection> findRevenueList(
      @Param("storeType") String storeType,
      @Param("creatorId") Long creatorId,
      Pageable pageable
  );


  // ============================
  // 상세 – 기본 정보
  // ============================
  @Query(value = """
    SELECT
        r.revenue_id          AS revenueId,
        p.project_id          AS projectId,
        c.contract_id         AS contractId,

        p.title               AS projectTitle,
        p.type                AS projectType,

        c.contract_title      AS contractTitle,
        c.contract_type       AS contractType,

        cc.company_name       AS clientCompanyName,
        cl.name               AS clientName,
        sm.name               AS salesManagerName,

        c.contract_amount     AS depositAmount,
        r.base_rent_snapshot  AS baseRentSnapshot,
        c.commission_rate     AS commissionRate,
        c.payment_condition   AS paymentCondition,
        c.currency            AS currency,

        c.contract_start_date AS contractStartDate,
        c.contract_end_date   AS contractEndDate

    FROM revenue r

    --  방어: revenue는 반드시 존재한다 → LEFT JOIN 안전
    LEFT JOIN project p
        ON p.project_id = r.project_id

    LEFT JOIN contract c
        ON c.contract_id = r.contract_id

    LEFT JOIN client_company cc
        ON cc.client_company_id = r.client_company_id

    LEFT JOIN client cl
        ON cl.client_id = r.client_id

    LEFT JOIN user sm
        ON sm.user_id = p.sales_manager_id

    WHERE r.revenue_id = :revenueId
    """,
          nativeQuery = true
  )
  RevenueDetailProjection findRevenueDetail(@Param("revenueId") Long revenueId);

  // ============================
  // 상세 – 매장 정보 리스트
  // ============================
  @Query(value = """
    SELECT
        stm.store_tenant_map_id     AS storeTenantMapId,
        f.floor_name                AS floorName,
        s.store_number              AS storeNumber,
        scm.final_contract_amount   AS finalContractAmount,
        stm.store_display_name      AS storeDisplayName
    FROM STORE_TENANT_MAP stm
    JOIN store s
        ON s.store_id = stm.store_id
    JOIN floor f
        ON f.floor_id = s.floor_id
    LEFT JOIN store_contract_map scm
        ON scm.contract_id = stm.contract_id
       AND scm.store_id = stm.store_id
    WHERE stm.contract_id = :contractId
    """,
      nativeQuery = true
  )
  List<StoreInfoProjection> findStoreInfosByContract(@Param("contractId") Long contractId);

  // ============================
  // 상세 – 누적 정산 집계
  // ============================
  @Query(value = """
      SELECT
          COALESCE(SUM(rs.total_sales_amount), 0)   AS totalSalesAccumulated,
          COALESCE(SUM(rs.commission_amount), 0)    AS commissionAmountAccumulated,
          COALESCE(SUM(rs.final_revenue), 0)        AS finalRevenueAccumulated
      FROM revenue r
      JOIN revenue_settlement rs
          ON rs.contract_id = r.contract_id
      WHERE r.revenue_id = :revenueId
      """,
      nativeQuery = true
  )
  SettlementAggProjection findSettlementAgg(@Param("revenueId") Long revenueId);

  // ============================
  // 상세 – 최신 정산 한 건
  // ============================
  @Query(value = """
      SELECT
          rs.settlement_year      AS settlementYear,
          rs.settlement_month     AS settlementMonth,
          rs.total_sales_amount   AS totalSalesAmount,
          rs.commission_rate      AS commissionRate,
          rs.commission_amount    AS commissionAmount,
          rs.final_revenue        AS finalRevenue
      FROM revenue r
      JOIN revenue_settlement rs
          ON rs.contract_id = r.contract_id
      WHERE r.revenue_id = :revenueId
      ORDER BY rs.settlement_year DESC,
               rs.settlement_month DESC
      LIMIT 1
      """,
      nativeQuery = true
  )
  LatestSettlementProjection findLatestSettlement(@Param("revenueId") Long revenueId);

  @Query("""
        select rs
        from RevenueSettlement rs
        where rs.contractId = :contractId
          and (rs.settlementYear * 100 + rs.settlementMonth) between :startYm and :endYm
        order by rs.settlementYear asc, rs.settlementMonth asc
        """)
  List<RevenueSettlement> findHistoryByContractAndYearMonthBetween(
      @Param("contractId") Long contractId,
      @Param("startYm") int startYm,
      @Param("endYm") int endYm
  );

  @Query("""
      select r
      from Revenue r
      where r.project = :project
        and r.isDeleted = false
      order by r.createdAt asc
      """)
  List<Revenue> findByProjectForHistory(@Param("project") Project project);

  @Query("""
      select r
      from Revenue r
      where r.project.id = :projectId
        and r.isDeleted = false
      order by r.createdAt desc
      """)
  List<Revenue> findActiveByProjectId(@Param("projectId") Long projectId);
}
