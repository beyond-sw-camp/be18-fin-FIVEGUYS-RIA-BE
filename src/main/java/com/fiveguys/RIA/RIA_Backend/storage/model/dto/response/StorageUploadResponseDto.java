package com.fiveguys.RIA.RIA_Backend.storage.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageUploadResponseDto {

    private Long fileId;
    private String uploadUrl;
    private String s3Key;
    private String originalName;
    private String mimeType;
}
