package com.fiveguys.RIA.RIA_Backend.storemap.model.repository;

import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreTenantMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreTenantMapRepository extends JpaRepository<StoreTenantMap, Long> {

    Optional<StoreTenantMap> findByStoreIdAndStatus(Long storeId, StoreTenantMap.Status status);

    List<StoreTenantMap> findByStatus(StoreTenantMap.Status status);
}
