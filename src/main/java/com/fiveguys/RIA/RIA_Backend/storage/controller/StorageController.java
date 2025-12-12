package com.fiveguys.RIA.RIA_Backend.storage.controller;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.PageResponse;
import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.request.StorageUploadRequestDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageDeleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageDownloadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageUploadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "파일 저장소", description = "S3 파일 업로드/다운로드/삭제 및 저장소 조회 API")
@RestController
@RequestMapping("/api/storages")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @Operation(
            summary = "전체 파일 목록 조회",
            description = "전체 파일 저장소를 페이지 단위로 조회합니다. (권한: 로그인 사용자)"
    )
    @GetMapping
    public ResponseEntity<PageResponse<StorageResponseDto>> getStorages(
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Page<StorageResponseDto> storages =
                storageService.getStorages(pageable, loginUser.getUserId());
        return ResponseEntity.ok(PageResponse.of(storages));
    }

    @Operation(
            summary = "파일 업로드 Presigned URL 생성",
            description = "S3 업로드용 Presigned URL을 생성합니다. URL에 직접 PUT 요청해야 합니다."
    )
    @PostMapping("/upload")
    public ResponseEntity<StorageUploadResponseDto> createUploadUrl(
            @RequestBody StorageUploadRequestDto request,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        StorageUploadResponseDto response =
                storageService.createUploadUrl(request, loginUser.getUserId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "파일 삭제",
            description = "지정한 파일을 삭제합니다. 본인이 업로드한 파일만 삭제 가능합니다."
    )
    @DeleteMapping("/{fileId}")
    public ResponseEntity<StorageDeleteResponseDto> deleteFile(
            @PathVariable("fileId") Long fileId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        storageService.deleteFile(fileId, loginUser.getUserId());
        StorageDeleteResponseDto response =
                new StorageDeleteResponseDto(true, "파일이 정상적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "파일 다운로드 Presigned URL 생성",
            description = "지정한 파일을 다운로드하기 위한 Presigned URL을 생성합니다."
    )
    @GetMapping("/{fileId}/download")
    public ResponseEntity<StorageDownloadResponseDto> getDownload(
            @PathVariable Long fileId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        StorageDownloadResponseDto response =
                storageService.createDownloadUrl(fileId, loginUser.getUserId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "내가 업로드한 파일 조회",
            description = "로그인한 사용자가 업로드한 파일만 조회합니다."
    )
    @GetMapping("/my")
    public ResponseEntity<PageResponse<StorageResponseDto>> getMyFiles(
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Page<StorageResponseDto> result =
                storageService.getMyStorages(pageable, loginUser.getUserId());
        return ResponseEntity.ok(PageResponse.of(result));
    }
}
