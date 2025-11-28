package com.fiveguys.RIA.RIA_Backend.storage.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageResponseDto {

    private Long fileId;
    private String originalName;
    private String employeeNo;
    private String mimeType;
    private Long size;
    private LocalDateTime createdAt;

    private boolean canEdit; // 수정
    private boolean canDelete; // 삭제
}
