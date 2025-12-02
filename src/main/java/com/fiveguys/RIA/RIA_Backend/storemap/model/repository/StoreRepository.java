package com.fiveguys.RIA.RIA_Backend.storemap.model.repository;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("storemapStoreRepository")
public interface StoreRepository extends JpaRepository<Store, Long> {
}