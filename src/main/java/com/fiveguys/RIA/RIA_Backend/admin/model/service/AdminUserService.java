package com.fiveguys.RIA.RIA_Backend.admin.model.service;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.UserResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AdminUserService {
    User createUser(CreateUserRequestDto dto);
    List<Map<String, Object>> getRoles();
    void changeUserRole (Long userId, Long roleId);
    Page<UserResponseDto> getUsers(Pageable pageable);
    void deleteUser(Long userId);
}
