package com.fiveguys.RIA.RIA_Backend.storage.controller;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.PageResponse;
import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.request.StorageUploadRequestDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageUploadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/storages")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<PageResponse<StorageResponseDto>> getStorages(
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Page<StorageResponseDto> storages =
                storageService.getStorages(pageable, loginUser.getUserId());
        return ResponseEntity.ok(PageResponse.of(storages));
    }

    @PostMapping("/upload")
    public ResponseEntity<StorageUploadResponseDto> createUploadUrl(
            @RequestBody StorageUploadRequestDto request,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        StorageUploadResponseDto response =
                storageService.createUploadUrl(request, loginUser.getUserId());
        return ResponseEntity.ok(response);
    }


}
