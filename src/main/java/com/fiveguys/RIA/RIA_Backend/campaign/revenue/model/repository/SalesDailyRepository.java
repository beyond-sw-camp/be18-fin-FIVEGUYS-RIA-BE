package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SalesDailyRepository extends JpaRepository<SalesDaily, Long> {

  Optional<SalesDaily> findByStoreTenantMapIdAndSalesDate(Long storeTenantMapId,
      LocalDate salesDate);

  void deleteBySalesDate(LocalDate salesDate);


  List<SalesDaily> findBySalesDateBetween(LocalDate start, LocalDate end);

  List<SalesDaily> findByStoreTenantMapIdAndSalesDateBetweenOrderBySalesDateAsc(
      Long storeTenantMapId,
      LocalDate startDate,
      LocalDate endDate
  );

  @Query("""

      select
      sd.salesDate,
      stm.storeTenantMapId,
      coalesce(stm.storeDisplayName, s.storeNumber),
      sum(sd.totalSalesAmount),
      sum(sd.totalSalesCount)
    from SalesDaily sd,
         StoreTenantMap stm,
         Store s,
         Contract c,
         Project p
    where sd.salesDate between :startDate and :endDate
      and sd.storeTenantMapId = stm.storeTenantMapId
      and stm.store = s
      and stm.contract = c
      and c.project = p
      and p.salesManager.id = :managerId
      and p.type in (
        com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.POPUP,
        com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.EXHIBITION
      )
    group by sd.salesDate, stm.storeTenantMapId, stm.storeDisplayName, s.storeNumber
    order by sd.salesDate asc, stm.storeDisplayName asc
    """)
  List<Object[]> findPopupDailySalesByManagerAndPeriod(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("managerId") Long managerId
  );
}