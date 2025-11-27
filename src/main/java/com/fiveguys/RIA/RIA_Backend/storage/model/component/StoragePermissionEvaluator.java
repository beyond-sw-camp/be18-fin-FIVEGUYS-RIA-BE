package com.fiveguys.RIA.RIA_Backend.storage.model.component;

import com.fiveguys.RIA.RIA_Backend.auth.service.entity.Role;
import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StoragePermissionEvaluator {

    public boolean canEdit(Storage storage, User currentUser) {
        if (currentUser == null) return false;

        if (storage.getUploaderId().getId().equals(currentUser.getId())) {
            return true;
        }

        return isAdminOrLead(currentUser);
    }

    public boolean canDelete(Storage storage, User currentUser) {
        return canEdit(storage, currentUser);
    }

    public boolean canRead(Storage storage, User currentUser) {
        if (currentUser == null) return false;

        // 1) 파일 업로더 본인은 항상 다운로드 가능
        if (storage.getUploaderId().getId().equals(currentUser.getId())) {
            return true;
        }

        // 2) 관리자 + 팀장은 모든 파일 다운로드 가능
        return isAdminOrLead(currentUser);
    }

    private boolean isAdminOrLead(User user) {
        Role.RoleName roleName = user.getRole().getRoleName();
        return roleName == Role.RoleName.ROLE_ADMIN
                || roleName == Role.RoleName.ROLE_SALES_LEAD;
    }
}
