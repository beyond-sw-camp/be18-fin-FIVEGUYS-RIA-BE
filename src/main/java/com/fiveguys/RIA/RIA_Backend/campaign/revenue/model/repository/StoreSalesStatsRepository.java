package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.StoreSalesStats;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreSalesStatsRepository extends JpaRepository<StoreSalesStats, Long> {

  Optional<StoreSalesStats> findByStoreTenantMapId(Long storeTenantMapId);
}





