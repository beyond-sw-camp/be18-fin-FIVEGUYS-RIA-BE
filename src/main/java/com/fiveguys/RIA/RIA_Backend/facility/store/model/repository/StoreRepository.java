package com.fiveguys.RIA.RIA_Backend.facility.store.model.repository;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository <Store, Long> {

    // 층별 매장 조회 쿼리
    @Query("SELECT s FROM Store s WHERE s.floor.floorId = :floorId ORDER BY s.storeNumber ASC")
    List<Store> findStoresByFloor(Long floorId);

    //

    @Query("""
    SELECT s
    FROM Store s
    WHERE s.floor.floorId = :floorId
      AND s.status = :status
      AND (:type IS NULL OR s.type = :type)
      AND (:keyword IS NULL OR s.storeNumber LIKE %:keyword%)
    ORDER BY s.storeNumber ASC
""")
    List<Store> searchAvailableStores(Long floorId, Store.StoreStatus status, Store.StoreType type, String keyword);

}
