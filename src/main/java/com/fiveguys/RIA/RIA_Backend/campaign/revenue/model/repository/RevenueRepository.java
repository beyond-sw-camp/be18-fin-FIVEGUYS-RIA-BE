package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueListProjection;
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

  @Query(
      value = """
        SELECT
            r.revenue_id              AS revenueId,
            p.project_id              AS projectId,
            c.contract_id             AS contractId,
            rs.revenue_settlement_id  AS settlementId,

            c.contract_title          AS contractTitle,
            cc.company_name           AS clientCompanyName,

            s.type                    AS storeType,

            u.user_id                 AS managerId,
            u.name                    AS managerName,

            rs.settlement_year        AS settlementYear,
            rs.settlement_month       AS settlementMonth,
            rs.final_revenue          AS finalRevenue,

            c.contract_start_date     AS contractStartDay,   -- 추가
            c.contract_end_date       AS contractEndDay      -- 추가
        FROM REVENUE r
        JOIN PROJECT p
            ON p.project_id = r.project_id
        JOIN CONTRACT c
            ON c.contract_id = r.contract_id
        JOIN CLIENT_COMPANY cc
            ON cc.client_company_id = r.client_company_id
        JOIN STORE_CONTRACT_MAP stm
            ON stm.contract_id = c.contract_id
        JOIN STORE s
            ON s.store_id = stm.store_id
        JOIN USER u
            ON u.user_id = r.created_user
        JOIN (
            SELECT
                contract_id,
                MAX(settlement_year * 100 + settlement_month) AS latest_ym
            FROM REVENUE_SETTLEMENT
            GROUP BY contract_id
        ) latest
            ON latest.contract_id = c.contract_id
        JOIN REVENUE_SETTLEMENT rs
            ON rs.contract_id = c.contract_id
           AND (rs.settlement_year * 100 + rs.settlement_month) = latest.latest_ym
        WHERE (:storeType IS NULL OR s.type = :storeType)
          AND (:creatorId IS NULL OR r.created_user = :creatorId)
        ORDER BY rs.settlement_year DESC,
                 rs.settlement_month DESC,
                 r.revenue_id DESC
        """,
      countQuery = """
        SELECT COUNT(*)
        FROM REVENUE r
        """,
      nativeQuery = true
  )
  Page<RevenueListProjection> findRevenueList(
      @Param("storeType") String storeType,
      @Param("creatorId") Long creatorId,
      Pageable pageable
  );
}
