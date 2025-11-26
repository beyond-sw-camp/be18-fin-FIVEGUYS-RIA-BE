package com.fiveguys.RIA.RIA_Backend.admin.model.repository;

import com.fiveguys.RIA.RIA_Backend.admin.model.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface AdminRepository extends JpaRepository<AdminLog, Long> {
    @Modifying
    @Query("DELETE FROM AdminLog l WHERE l.createdAt < :threshold")
    int deleteByCreatedAtBefore(LocalDateTime threshold);
}
