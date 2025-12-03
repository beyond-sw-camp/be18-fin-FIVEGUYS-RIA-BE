package com.fiveguys.RIA.RIA_Backend.campaign.tenant.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.tenant.model.entity.StoreTenantMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreTenantMapRepository extends JpaRepository<StoreTenantMap, Long> {
}

