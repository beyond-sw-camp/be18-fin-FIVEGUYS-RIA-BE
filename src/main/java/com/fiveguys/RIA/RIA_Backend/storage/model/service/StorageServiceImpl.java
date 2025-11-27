package com.fiveguys.RIA.RIA_Backend.storage.model.service;

import com.fiveguys.RIA.RIA_Backend.storage.model.component.S3KeyGenerator;
import com.fiveguys.RIA.RIA_Backend.storage.model.component.S3PresignedUrlProvider;
import com.fiveguys.RIA.RIA_Backend.storage.model.component.StorageMapper;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.request.StorageUploadRequestDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageUploadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import com.fiveguys.RIA.RIA_Backend.storage.model.repository.StorageRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.component.UserLoader;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;
    private final UserLoader userLoader;
    private final StorageMapper storageMapper;
    private final S3KeyGenerator s3KeyGenerator;
    private final S3PresignedUrlProvider s3PresignedUrlProvider;

    @Override
    @Transactional(readOnly = true)
    public Page<StorageResponseDto> getStorages(Pageable pageable, Long userId) {

        User currentUser = userLoader.loadUser(userId);

        log.info("[StorageList-REQUEST] userId={}, page={}, size={}, sort={}",
                 currentUser.getId(),
                 pageable.getPageNumber(),
                 pageable.getPageSize(),
                 pageable.getSort()
        );

        Page<Storage> storages = storageRepository.findAll(pageable);

        log.info("[StorageList-RESULT] userId={}, page={}, size={}, totalElements={}, totalPages={}",
                 currentUser.getId(),
                 storages.getNumber(),
                 storages.getSize(),
                 storages.getTotalElements(),
                 storages.getTotalPages()
        );

        return storageMapper.toResponsePage(storages, currentUser);
    }

    @Override
    @Transactional
    public StorageUploadResponseDto createUploadUrl(StorageUploadRequestDto request, Long userId) {

        User currentUser = userLoader.loadUser(userId);

        String s3Key = s3KeyGenerator.generateKey("storages", request.getOriginalName());

        Storage storage = storageMapper.toEntity(request, currentUser, s3Key);

        Storage saved = storageRepository.save(storage);

        String uploadUrl = s3PresignedUrlProvider.createPutUrl(
                s3Key,
                request.getMimeType(),
                Duration.ofMinutes(10)
        );

        log.info("[StorageUpload-URL-CREATED] userId={}, fileId={}, expiresInMinutes={}",
                 currentUser.getId(),
                 saved.getFileId(),
                 10
        );

        return storageMapper.toUploadResponse(saved, uploadUrl);
    }

}
