package com.fiveguys.RIA.RIA_Backend.storage.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StorageDownloadResponseDto {

    private Long fileId;
    private String originalName;
    private String mimeType;
    private String downloadUrl;
}
