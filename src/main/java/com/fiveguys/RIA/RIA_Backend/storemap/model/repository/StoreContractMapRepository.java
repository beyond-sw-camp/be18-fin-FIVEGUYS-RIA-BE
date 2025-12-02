package com.fiveguys.RIA.RIA_Backend.storemap.model.repository;

import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreContractMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreContractMapRepository extends JpaRepository<StoreContractMap, Long> {

    List<StoreContractMap> findByStoreId(Long storeId);
}
