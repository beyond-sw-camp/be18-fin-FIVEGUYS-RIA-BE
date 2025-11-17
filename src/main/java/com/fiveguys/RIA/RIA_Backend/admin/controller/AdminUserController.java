package com.fiveguys.RIA.RIA_Backend.admin.controller;


import com.fiveguys.RIA.RIA_Backend.admin.model.dto.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.RoleChangeRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminRoleChangeService;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminRoleService;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminUserService;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AdminRoleService adminRoleService;
    private final AdminRoleChangeService adminRoleChangeService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDto dto) {
        User created = adminUserService.createUser(dto);
        return ResponseEntity.ok("사용자 생성 완료: " + created.getEmployeeNo());
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getRoles() {
        return ResponseEntity.ok(adminRoleService.getRoles());
    }

    @PatchMapping("/users/{userId}/change")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserRole(@PathVariable Long userId,
                                            @RequestBody RoleChangeRequestDto dto) {
        adminRoleChangeService.changeUserRole(userId, dto.getRoleId());
        return ResponseEntity.ok("권한 변경 완료");
    }
}
