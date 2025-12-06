package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesDaily;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

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

}