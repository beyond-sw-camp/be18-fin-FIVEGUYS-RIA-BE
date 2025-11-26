package com.fiveguys.RIA.RIA_Backend.storage.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StorageResponse {

    private Long fileId;
    private String originalName;
    private String employeeNo;
    private String mimeType;
    private Long size;

    private boolean canEdit; // 수정
    private boolean canDelete; // 삭제
}
