package com.fiveguys.RIA.RIA_Backend.admin.model.component;

import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminErrorCode;
import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminException;
import com.fiveguys.RIA.RIA_Backend.common.model.entity.Role;
import com.fiveguys.RIA.RIA_Backend.common.model.repository.RoleRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminLoader {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User loadUser(Long userId){
        return userRepository.findById(userId)
                             .orElseThrow(() -> new AdminException(AdminErrorCode.USER_NOT_FOUND));
    }

    public Role loadRole(Long roleId){
        return roleRepository.findById(roleId)
                             .orElseThrow(() -> new AdminException(AdminErrorCode.ROLE_NOT_FOUND));
    }
}
