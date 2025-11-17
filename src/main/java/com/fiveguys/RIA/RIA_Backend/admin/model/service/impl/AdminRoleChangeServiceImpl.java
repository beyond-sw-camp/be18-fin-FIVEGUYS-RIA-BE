package com.fiveguys.RIA.RIA_Backend.admin.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminErrorCode;
import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminException;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminRoleChangeService;
import com.fiveguys.RIA.RIA_Backend.common.model.entity.Role;
import com.fiveguys.RIA.RIA_Backend.common.model.repository.RoleRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRoleChangeServiceImpl implements AdminRoleChangeService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public void changeUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AdminException(
                AdminErrorCode.USER_NOT_FOUND));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new AdminException(
                AdminErrorCode.ROLE_NOT_FOUND));

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
