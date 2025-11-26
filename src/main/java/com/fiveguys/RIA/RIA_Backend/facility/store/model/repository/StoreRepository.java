package com.fiveguys.RIA.RIA_Backend.facility.store.model.repository;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository <Store, Long> {
    @Query("SELECT s FROM Store s WHERE s.floorId.floorId = :floorId ORDER BY s.storeNumber ASC")
    List<Store> findStoresByFloor(Long floorId);
}
