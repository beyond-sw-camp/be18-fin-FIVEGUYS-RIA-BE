package com.fiveguys.RIA.RIA_Backend.admin.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.admin.model.component.AdminLoader;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminErrorCode;
import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminException;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminUserService;
import com.fiveguys.RIA.RIA_Backend.auth.service.entity.Role;
import com.fiveguys.RIA.RIA_Backend.auth.service.repository.RoleRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminLoader adminLoader;

  @Transactional
  @Override
  public User createUser(CreateUserRequestDto dto) {
    if (userRepository.existsByEmployeeNo(dto.getEmployeeNo())) {
      throw new AdminException(AdminErrorCode.USER_ALREADY_EXISTS);
    }


    Role role = adminLoader.loadRole(dto.getRoleId());

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

  @Transactional(readOnly = true)
  @Override
  public List<Map<String, Object>> getRoles() {
    return roleRepository.findAll().stream()
                         .map(r -> Map.<String, Object>of(
                                 "id", r.getId(),
                                 "name", r.getRoleName().name(),
                                 "label", switch (r.getRoleName()) {
                                   case ROLE_ADMIN -> "관리자";
                                   case ROLE_SALES_LEAD -> "영업팀장";
                                   case ROLE_SALES_MEMBER -> "영업사원";
                                 }
                         ))
                         .toList();
  }

  @Transactional
  @Override
  public void changeUserRole(Long userId, Long roleId) {

    User user = adminLoader.loadUser(userId);
    Role role = adminLoader.loadRole(roleId);

    String position = user.getPosition();

    if (roleId.equals(3L)) {
      position = "영업팀원";
    } else if (roleId.equals(2L)) {
      position = "영업팀장";
    }

    user.changeRoleAndPosition(role, position);

    log.info("사용자 ID {}의 직책이 '{}'로 변경되었습니다.", userId, position);
  }
}
