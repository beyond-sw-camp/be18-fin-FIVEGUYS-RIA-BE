package com.fiveguys.RIA.RIA_Backend.storemap.model.repository;

import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreSalesStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreSalesStatsRepository extends JpaRepository<StoreSalesStats, Long> {

    Optional<StoreSalesStats> findByStoreTenantMapId(Long tenantMapId);
}
