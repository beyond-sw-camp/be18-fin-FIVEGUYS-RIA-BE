package com.fiveguys.RIA.RIA_Backend.storage.controller;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.PageResponse;
import com.fiveguys.RIA.RIA_Backend.storage.model.service.StorageService;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.data.domain.Pageable;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.StorageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/storages")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping()
    public ResponseEntity<PageResponse<StorageResponse>> getStorages(Pageable pageable,@AuthenticationPrincipal
    User loginUser){
        Page<StorageResponse> storages = storageService.getStorages(pageable, loginUser);
        return ResponseEntity.ok(PageResponse.of(storages));
    }
}
