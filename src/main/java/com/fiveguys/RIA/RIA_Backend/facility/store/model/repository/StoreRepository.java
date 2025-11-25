package com.fiveguys.RIA.RIA_Backend.facility.store.model.repository;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository <Store, Long> {
}
