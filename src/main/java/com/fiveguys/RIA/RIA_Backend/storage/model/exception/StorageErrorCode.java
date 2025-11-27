package com.fiveguys.RIA.RIA_Backend.storage.model.exception;

import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StorageErrorCode implements ErrorCode {

    STORAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "STO-001", "파일을 찾을 수 없습니다."),
    FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "STO-002", "이 파일을 삭제할 권한이 없습니다."),
    S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "STO-003", "S3 파일 삭제에 실패했습니다."),
    FORBIDDEN_DOWNLOAD(HttpStatus.FORBIDDEN, "STO-004", "이 파일을 다운로드할 권한이 없습니다."); // ✅ 추가

    private final HttpStatus status;
    private final String code;
    private final String message;
}
