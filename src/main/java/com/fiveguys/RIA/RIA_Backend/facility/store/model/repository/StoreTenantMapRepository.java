package com.fiveguys.RIA.RIA_Backend.facility.store.model.repository;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.Optional;

@Repository
public interface StoreTenantMapRepository extends JpaRepository<StoreTenantMap, Long> {
    Optional<StoreTenantMap> findFirstByStore_StoreIdAndStatus(
            Long storeId,
            StoreTenantMap.Status status
    );
=======
import java.util.List;

@Repository
public interface StoreTenantMapRepository extends JpaRepository<StoreTenantMap, Long> {
    List<StoreTenantMap> findByStoreAndStatus(Store store, StoreTenantMap.Status status);
    StoreTenantMap findTopByClientCompanyOrderByStartDateDesc(ClientCompany clientCompany);

>>>>>>> origin
}

