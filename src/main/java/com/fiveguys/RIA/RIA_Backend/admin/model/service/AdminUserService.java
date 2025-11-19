package com.fiveguys.RIA.RIA_Backend.admin.model.service;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;

import java.util.List;
import java.util.Map;

public interface AdminUserService {
    User createUser(CreateUserRequestDto dto);
    List<Map<String, Object>> getRoles();
    void changeUserRole (Long userId, Long roleId);
}
