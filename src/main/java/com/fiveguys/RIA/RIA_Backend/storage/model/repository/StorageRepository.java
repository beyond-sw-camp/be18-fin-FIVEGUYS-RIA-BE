package com.fiveguys.RIA.RIA_Backend.storage.model.repository;

import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage,Long> {
}
