package com.fiveguys.RIA.RIA_Backend.storage.model.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageUploadRequestDto {
    private String originalName;
    private String mimeType;
    private Long size;

}
