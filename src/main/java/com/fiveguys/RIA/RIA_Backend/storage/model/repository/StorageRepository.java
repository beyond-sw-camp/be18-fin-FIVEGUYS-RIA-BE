package com.fiveguys.RIA.RIA_Backend.storage.model.repository;

import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage,Long> {
    Page<Storage> findAllByUploaderId(User uploader, Pageable pageable);
}
