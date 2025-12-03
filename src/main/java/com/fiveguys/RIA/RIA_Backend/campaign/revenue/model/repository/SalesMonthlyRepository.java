package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesMonthly;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesMonthlyRepository extends JpaRepository<SalesMonthly, Long> {

  Optional<SalesMonthly> findByStoreTenantMapIdAndSalesYearAndSalesMonth(
      Long storeTenantMapId,
      int salesYear,
      int salesMonth
  );

  List<SalesMonthly> findBySalesYear(int salesYear);

}