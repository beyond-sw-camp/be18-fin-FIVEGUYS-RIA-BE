package com.fiveguys.RIA.RIA_Backend.admin.controller;


import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.AdminLogResponseDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.RoleChangeRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminLogService;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminUserService;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AdminLogService adminLogService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDto dto) {
        User created = adminUserService.createUser(dto);
        return ResponseEntity.ok("사용자 생성 완료: " + created.getEmployeeNo());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Map<String, Object>>> getRoles() {
        return ResponseEntity.ok(adminUserService.getRoles());
    }

    @PatchMapping("/users/{userId}/changes")
    public ResponseEntity<?> changeUserRole(@PathVariable Long userId,
                                            @RequestBody RoleChangeRequestDto dto) {
        adminUserService.changeUserRole(userId, dto.getRoleId());
        return ResponseEntity.ok("권한 변경 완료");
    }

    @GetMapping("/logs")
    public ResponseEntity<Page<AdminLogResponseDto>> getLogs(Pageable pageable) {
        Page<AdminLogResponseDto> logs = adminLogService.getLogs(pageable);
        return ResponseEntity.ok(logs);
    }
}
