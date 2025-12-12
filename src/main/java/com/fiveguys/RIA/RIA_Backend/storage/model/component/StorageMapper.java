package com.fiveguys.RIA.RIA_Backend.storage.model.component;

import com.fiveguys.RIA.RIA_Backend.storage.model.dto.request.StorageUploadRequestDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageDownloadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageUploadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageMapper {

    private final StoragePermissionEvaluator permissionEvaluator;

    public StorageResponseDto toResponse(Storage storage, User currentUser) {
        boolean canEdit = permissionEvaluator.canEdit(storage, currentUser);
        boolean canDelete = permissionEvaluator.canDelete(storage, currentUser);

        return StorageResponseDto.builder()
                                 .fileId(storage.getFileId())
                                 .originalName(storage.getOriginalName())
                                 .employeeNo(storage.getUploaderId().getEmployeeNo())
                                 .mimeType(storage.getMimeType())
                                 .size(storage.getSize())
                                 .createdAt(storage.getCreatedAt())
                                 .canEdit(canEdit)
                                 .canDelete(canDelete)
                                 .build();
    }

    public Page<StorageResponseDto> toResponsePage(Page<Storage> storages, User currentUser) {
        return storages.map(storage -> toResponse(storage, currentUser));
    }

    public Storage toEntity(StorageUploadRequestDto request, User currentUser, String s3Key) {
        return Storage.builder()
                      .uploaderId(currentUser)
                      .s3Key(s3Key)
                      .originalName(request.getOriginalName())
                      .mimeType(request.getMimeType())
                      .size(request.getSize())
                      .type(request.getType())
                      .build();
    }

    public StorageUploadResponseDto toUploadResponse(Storage storage, String uploadUrl) {
        return StorageUploadResponseDto.builder()
                                       .fileId(storage.getFileId())
                                       .uploadUrl(uploadUrl)
                                       .s3Key(storage.getS3Key())
                                       .originalName(storage.getOriginalName())
                                       .mimeType(storage.getMimeType())
                                       .build();
    }

    public StorageDownloadResponseDto toDownloadResponse(Storage storage, String downloadUrl) {
        return StorageDownloadResponseDto.builder()
                                         .fileId(storage.getFileId())
                                         .originalName(storage.getOriginalName())
                                         .mimeType(storage.getMimeType())
                                         .downloadUrl(downloadUrl)
                                         .build();
    }

}
