package com.fiveguys.RIA.RIA_Backend.common.model.repository;

import com.fiveguys.RIA.RIA_Backend.common.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
