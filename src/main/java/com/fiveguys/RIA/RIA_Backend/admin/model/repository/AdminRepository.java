package com.fiveguys.RIA.RIA_Backend.admin.model.repository;

import com.fiveguys.RIA.RIA_Backend.admin.model.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminLog, Long> {
}
