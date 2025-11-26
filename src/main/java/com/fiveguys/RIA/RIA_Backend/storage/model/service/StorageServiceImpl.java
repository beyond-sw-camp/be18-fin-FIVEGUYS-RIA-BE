package com.fiveguys.RIA.RIA_Backend.storage.model.service;

import com.fiveguys.RIA.RIA_Backend.storage.model.dto.StorageResponse;
import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import com.fiveguys.RIA.RIA_Backend.storage.model.repository.StorageRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.auth.service.entity.Role; // ⭐ 이거 import 필요
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<StorageResponse> getStorages(Pageable pageable, User currentUser) {
        return storageRepository.findAll(pageable)
                                .map(storage -> toResponse(storage, currentUser));
    }

    private StorageResponse toResponse(Storage storage, User currentUser) {
        boolean canEdit = canEdit(storage, currentUser);
        boolean canDelete = canDelete(storage, currentUser);

        return StorageResponse.builder()
                              .fileId(storage.getFileId())
                              .originalName(storage.getOriginalName())
                              .employeeNo(storage.getUploaderId().getEmployeeNo())
                              .mimeType(storage.getMimeType())
                              .size(storage.getSize())
                              .canEdit(canEdit)
                              .canDelete(canDelete)
                              .build();
    }

    private boolean canEdit(Storage storage, User currentUser) {
        if (currentUser == null) {
            return false;
        }
        if (storage.getUploaderId().getId().equals(currentUser.getId())) {
            return true;
        }
        return isAdminOrLead(currentUser);
    }

    private boolean canDelete(Storage storage, User currentUser) {
        return canEdit(storage, currentUser);
    }

    private boolean isAdminOrLead(User user) {
        Role.RoleName roleName = user.getRole().getRoleName();
        return roleName == Role.RoleName.ROLE_ADMIN
                || roleName == Role.RoleName.ROLE_SALES_LEAD;
    }
}
