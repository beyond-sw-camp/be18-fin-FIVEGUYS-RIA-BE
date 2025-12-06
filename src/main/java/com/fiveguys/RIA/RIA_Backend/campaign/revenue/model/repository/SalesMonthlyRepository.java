package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreMonthlySalesItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SalesMonthlyRepository extends JpaRepository<SalesMonthly, Long> {

  Optional<SalesMonthly> findByStoreTenantMapIdAndSalesYearAndSalesMonth(
      Long storeTenantMapId,
      int salesYear,
      int salesMonth
  );

  List<SalesMonthly> findBySalesYear(int salesYear);

  /**
   * 1번 그래프: 입점(RENTAL) 프로젝트 기준 매장별 월 매출
   */
  @Query("""
    select new com.fiveguys.RIA.RIA_Backend.dashboard.model.dto.response.StoreMonthlySalesItem(
        stm.storeTenantMapId,
        coalesce(stm.storeDisplayName, s.storeNumber),
        sum(sm.totalSalesAmount),
        sum(sm.totalSalesCount)
    )
    from SalesMonthly sm
      join StoreTenantMap stm
        on stm.storeTenantMapId = sm.storeTenantMapId
      join Store s
        on s = stm.store
      join Contract c
        on c = stm.contract
      join Project p
        on p = c.project
    where sm.salesYear  = :year
      and sm.salesMonth = :month
      and p.salesManager.id = :managerId
      and p.type = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.RENTAL
    group by stm.storeTenantMapId, stm.storeDisplayName, s.storeNumber
    order by sum(sm.totalSalesAmount) desc
    """)
  List<StoreMonthlySalesItem> findStoreMonthlySalesByManager(
      @Param("year") int year,
      @Param("month") int month,
      @Param("managerId") Long managerId
  );

  /**
   * 기준 연월보다 이전 데이터 존재 여부 (입점만)
   */
  @Query("""
        select
            case when count(sm) > 0 then true else false end
        from SalesMonthly sm
          join StoreTenantMap stm
            on stm.storeTenantMapId = sm.storeTenantMapId
          join Contract c
            on c = stm.contract
          join Project p
            on p = c.project
        where (sm.salesYear * 100 + sm.salesMonth) < :ym
          and p.type = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.RENTAL
        """)
  boolean existsBefore(@Param("ym") int ym);

  /**
   * 기준 연월보다 이후 데이터 존재 여부 (입점만)
   */
  @Query("""
        select
            case when count(sm) > 0 then true else false end
        from SalesMonthly sm
          join StoreTenantMap stm
            on stm.storeTenantMapId = sm.storeTenantMapId
          join Contract c
            on c = stm.contract
          join Project p
            on p = c.project
        where (sm.salesYear * 100 + sm.salesMonth) > :ym
          and p.type = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.RENTAL
        """)
  boolean existsAfter(@Param("ym") int ym);

  /**
   * 가장 최신 연/월 목록 (입점만)
   */
  @Query("""
        select distinct sm.salesYear, sm.salesMonth
        from SalesMonthly sm
          join StoreTenantMap stm
            on stm.storeTenantMapId = sm.storeTenantMapId
          join Contract c
            on c = stm.contract
          join Project p
            on p = c.project
        where p.type = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.RENTAL
        order by sm.salesYear desc, sm.salesMonth desc
        """)
  List<Object[]> findAllYearMonthOrderByLatest();


  // 매장별 월 실적 + 프로젝트 목표(입점만)
  @Query("""
      select
        stm.storeTenantMapId,
        coalesce(stm.storeDisplayName, s.storeNumber),
        sum(sm.totalSalesAmount),
        p.expectedRevenue,
        p.expectedMarginRate
      from SalesMonthly sm
        join StoreTenantMap stm
          on stm.storeTenantMapId = sm.storeTenantMapId
        join Store s
          on s = stm.store
        join Contract c
          on c = stm.contract
        join Project p
          on p = c.project
      where sm.salesYear  = :year
        and sm.salesMonth = :month
        and p.salesManager.id = :managerId
        and p.type = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.RENTAL
      group by stm.storeTenantMapId, stm.storeDisplayName, s.storeNumber,
               p.expectedRevenue, p.expectedMarginRate
      order by sum(sm.totalSalesAmount) desc
      """)
  List<Object[]> findStoreMonthlyPerformanceByManager(
      @Param("year") int year,
      @Param("month") int month,
      @Param("managerId") Long managerId
  );


  @Query("""
    select
      stm.storeTenantMapId,
      coalesce(stm.storeDisplayName, s.storeNumber),
      sum(sm.totalSalesAmount)
    from SalesMonthly sm
      join StoreTenantMap stm
        on stm.storeTenantMapId = sm.storeTenantMapId
      join Store s
        on s = stm.store
      join Contract c
        on c = stm.contract
      join Project p
        on p = c.project
    where sm.salesYear  = :year
      and sm.salesMonth = :month
      and p.salesManager.id = :managerId
      and p.type = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.RENTAL
    group by stm.storeTenantMapId, stm.storeDisplayName, s.storeNumber
    order by sum(sm.totalSalesAmount) desc
    """)
  List<Object[]> findBrandMonthlySalesByManager(
      @Param("year") int year,
      @Param("month") int month,
      @Param("managerId") Long managerId
  );
}
