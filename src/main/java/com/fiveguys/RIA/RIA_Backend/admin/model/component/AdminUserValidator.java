package com.fiveguys.RIA.RIA_Backend.admin.model.component;

import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminErrorCode;
import com.fiveguys.RIA.RIA_Backend.admin.model.exception.AdminException;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserValidator {

    public void validateDeletable(User user) {
        if (user.isDeleted()) {
            throw new AdminException(AdminErrorCode.FORBIDDEN_OPERATION);
        }
    }
}

