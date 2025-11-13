package com.fiveguys.RIA.RIA_Backend.admin.model.service.impl;


import com.fiveguys.RIA.RIA_Backend.admin.model.dto.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminErrorCode;
import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminException;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminUserService;
import com.fiveguys.RIA.RIA_Backend.common.model.entity.Role;
import com.fiveguys.RIA.RIA_Backend.common.model.repository.RoleRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  @Override
  public User createUser(CreateUserRequestDto dto) {
    if (userRepository.existsByEmployeeNo(dto.getEmployeeNo())) {
      throw new AdminException(AdminErrorCode.USER_ALREADY_EXISTS);
    }

    Role role = roleRepository.findById(dto.getRoleId())
        .orElseThrow(() -> new AdminException(AdminErrorCode.ROLE_NOT_FOUND));

    User user = User.builder()
        .employeeNo(dto.getEmployeeNo())
        .name(dto.getName())
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .department(User.Department.valueOf(dto.getDepartment()))
        .position(dto.getPosition())
        .role(role)
        .build();

    return userRepository.save(user);
  }
}
