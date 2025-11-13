package com.fiveguys.RIA.RIA_Backend.admin.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminRoleService;
import com.fiveguys.RIA.RIA_Backend.common.model.repository.RoleRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRoleServiceImpl implements AdminRoleService {

  private final RoleRepository roleRepository;

  @Override
  public List<Map<String, Object>> getRoles() {
    return roleRepository.findAll().stream()
        .map(r -> Map.<String, Object>of(
            "id", r.getId(),
            "name", r.getRoleName().name(), //
            "label", switch (r.getRoleName()) {
              case ROLE_ADMIN -> "관리자";
              case ROLE_SALES_LEAD -> "영업팀장";
              case ROLE_SALES_MEMBER -> "영업사원";
            }
        ))
        .toList();
  }
}