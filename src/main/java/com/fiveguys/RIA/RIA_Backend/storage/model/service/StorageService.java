package com.fiveguys.RIA.RIA_Backend.storage.model.service;

import com.fiveguys.RIA.RIA_Backend.storage.model.dto.request.StorageUploadRequestDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageDownloadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageUploadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StorageService {

    Page<StorageResponseDto> getStorages(Pageable pageable, Long userId);
    StorageUploadResponseDto createUploadUrl(StorageUploadRequestDto request, Long userId);
    void deleteFile(Long fileId, Long userId);
    StorageDownloadResponseDto createDownloadUrl(Long fileId, Long userId);
}
