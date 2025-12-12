package com.fiveguys.RIA.RIA_Backend.storage.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageUploadRequestDto {
    private String originalName;
    private String mimeType;
    private Long size;
    private Storage.StorageType type;
}
