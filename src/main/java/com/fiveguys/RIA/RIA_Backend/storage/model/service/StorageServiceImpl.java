package com.fiveguys.RIA.RIA_Backend.storage.model.service;

import com.fiveguys.RIA.RIA_Backend.storage.model.component.s3.S3KeyGenerator;
import com.fiveguys.RIA.RIA_Backend.storage.model.component.s3.S3ObjectDeleter;
import com.fiveguys.RIA.RIA_Backend.storage.model.component.s3.S3PresignedUrlProvider;
import com.fiveguys.RIA.RIA_Backend.storage.model.component.StorageLoader;
import com.fiveguys.RIA.RIA_Backend.storage.model.component.StorageMapper;
import com.fiveguys.RIA.RIA_Backend.storage.model.component.StoragePermissionEvaluator;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.request.StorageUploadRequestDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageDownloadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageUploadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import com.fiveguys.RIA.RIA_Backend.storage.model.exception.StorageErrorCode;
import com.fiveguys.RIA.RIA_Backend.storage.model.exception.StorageException;
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
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;
    private final UserLoader userLoader;
    private final StorageMapper storageMapper;
    private final S3KeyGenerator s3KeyGenerator;
    private final S3PresignedUrlProvider s3PresignedUrlProvider;
    private final StorageLoader storageLoader;
    private final StoragePermissionEvaluator permissionEvaluator;
    private final S3ObjectDeleter s3ObjectDeleter;

    @Override
    @Transactional(readOnly = true)
    public Page<StorageResponseDto> getStorages(Pageable pageable, Long userId) {

        User currentUser = userLoader.loadUser(userId);

        Page<Storage> storages = storageRepository.findAll(pageable);

        log.info("[목록 조회 결과] userId={}, page={}, size={}, totalElements={}, totalPages={}",
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
                Duration.ofMinutes(5)
        );

        log.info("[PresignedUrl 생성] userId={}, fileId={}, expiresInMinutes={}",
                 currentUser.getId(),
                 saved.getFileId(),
                 5
        );

        return storageMapper.toUploadResponse(saved, uploadUrl);
    }

    @Override
    @Transactional
    public void deleteFile(Long fileId, Long userId) {

        User currentUser = userLoader.loadUser(userId);
        Storage storage = storageLoader.loadStorage(fileId);

        // 권한 체크
        if (!permissionEvaluator.canDelete(storage, currentUser)) {
            log.warn("[삭제권한이 없음] userId={}, fileId={}", currentUser.getId(), fileId);
            throw new StorageException(StorageErrorCode.FORBIDDEN_DELETE);
        }

        String s3Key = storage.getS3Key();

        // 1) S3에서 삭제
        try {
            s3ObjectDeleter.deleteObject(s3Key);
        } catch (S3Exception e) {
            log.error("[S3에서 삭제 실패] userId={}, fileId={}, s3Key={}, message={}",
                      currentUser.getId(), fileId, s3Key, e.getMessage(), e);
            throw new StorageException(StorageErrorCode.S3_DELETE_FAILED);
        }

        // 2) DB에서 삭제
        storageRepository.delete(storage);

        log.info("[삭제 성공] userId={}, fileId={}, s3Key={}",
                 currentUser.getId(), fileId, s3Key);
    }

    @Override
    @Transactional
    public StorageDownloadResponseDto createDownloadUrl(Long fileId, Long userId) {

        User user = userLoader.loadUser(userId);
        Storage storage = storageLoader.loadStorage(fileId);

        if (!permissionEvaluator.canRead(storage, user)) {
            throw new StorageException(StorageErrorCode.FORBIDDEN_DOWNLOAD);
        }

        String downloadUrl = s3PresignedUrlProvider.createGetUrl(
                storage.getS3Key(),
                storage.getOriginalName(),
                Duration.ofMinutes(5)
        );

        log.info("[다운로드 성공] userId={}, fileId={}, s3Key={}, originalName={}",
                 user.getId(), fileId, storage.getS3Key(), storage.getOriginalName());

        return storageMapper.toDownloadResponse(storage, downloadUrl);
    }

    @Override
    @Transactional
    public Page<StorageResponseDto> getMyStorages(Pageable pageable, Long userId) {

        User currentUser = userLoader.loadUser(userId);

        Page<Storage> storages =
                storageRepository.findAllByUploaderId(currentUser, pageable);

        return storageMapper.toResponsePage(storages, currentUser);
    }

}
