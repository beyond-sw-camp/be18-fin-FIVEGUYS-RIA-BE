package com.fiveguys.RIA.RIA_Backend.storage.model.component;

import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import com.fiveguys.RIA.RIA_Backend.storage.model.exception.StorageException;
import com.fiveguys.RIA.RIA_Backend.storage.model.exception.StorageErrorCode;
import com.fiveguys.RIA.RIA_Backend.storage.model.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageLoader {

    private final StorageRepository storageRepository;

    public Storage loadStorage(Long fileId) {
        return storageRepository.findById(fileId)
                                .orElseThrow(() -> new StorageException(StorageErrorCode.STORAGE_NOT_FOUND));
    }
}
