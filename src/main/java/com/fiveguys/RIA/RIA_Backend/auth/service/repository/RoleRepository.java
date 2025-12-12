package com.fiveguys.RIA.RIA_Backend.auth.service.repository;

import com.fiveguys.RIA.RIA_Backend.auth.service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
