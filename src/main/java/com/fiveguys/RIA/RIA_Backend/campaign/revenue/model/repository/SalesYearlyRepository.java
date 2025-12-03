package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.SalesYearly;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesYearlyRepository extends JpaRepository<SalesYearly, Long> {

  Optional<SalesYearly> findByStoreTenantMapIdAndSalesYear(Long storeTenantMapId, int salesYear);

}
