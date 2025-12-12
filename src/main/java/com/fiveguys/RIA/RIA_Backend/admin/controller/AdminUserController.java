package com.fiveguys.RIA.RIA_Backend.admin.controller;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.AdminLogResponseDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.RoleChangeRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.PageResponse;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.UserResponseDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminLogService;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminUserService;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "관리자 - 사용자 관리", description = "관리자 사용자의 생성, 권한 변경, 삭제 및 로그/사용자 조회 API")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AdminLogService adminLogService;

    @Operation(
            summary = "사용자 생성",
            description = "관리자가 신규 사용자를 생성합니다."
    )
    @PostMapping("/create")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody CreateUserRequestDto dto
    ) {
        User created = adminUserService.createUser(dto);
        return ResponseEntity.ok("사용자 생성 완료: " + created.getEmployeeNo());
    }

    @Operation(
            summary = "권한 목록 조회",
            description = "관리자가 부여할 수 있는 권한(Role) 목록을 조회합니다."
    )
    @GetMapping("/roles")
    public ResponseEntity<List<Map<String, Object>>> getRoles() {
        return ResponseEntity.ok(adminUserService.getRoles());
    }

    @Operation(
            summary = "사용자 권한 변경",
            description = "특정 사용자의 권한(Role)을 변경합니다."
    )
    @PatchMapping("/users/{userId}/changes")
    public ResponseEntity<?> changeUserRole(
            @Parameter(description = "권한을 변경할 사용자 ID", example = "1")
            @PathVariable Long userId,
            @RequestBody RoleChangeRequestDto dto
    ) {
        adminUserService.changeUserRole(userId, dto.getRoleId());
        return ResponseEntity.ok("권한 변경 완료");
    }

    @Operation(
            summary = "관리 로그 목록 조회",
            description = "관리자 액션 로그를 페이지네이션 형태로 조회합니다."
    )
    @GetMapping("/logs")
    public ResponseEntity<PageResponse<AdminLogResponseDto>> getLogs(
            @Parameter(hidden = true) Pageable pageable
    ) {
        Page<AdminLogResponseDto> logs = adminLogService.getLogs(pageable);
        return ResponseEntity.ok(PageResponse.of(logs));
    }

    @Operation(
            summary = "관리 대상 사용자 목록 조회",
            description = "관리자가 관리하는 사용자 목록을 페이지네이션 형태로 조회합니다."
    )
    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserResponseDto>> getUsers(
            @Parameter(hidden = true) Pageable pageable
    ) {
        Page<UserResponseDto> users = adminUserService.getUsers(pageable);
        return ResponseEntity.ok(PageResponse.of(users));
    }

    @Operation(
            summary = "사용자 삭제",
            description = "특정 사용자를 삭제(또는 소프트 삭제)합니다."
    )
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "삭제할 사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        adminUserService.deleteUser(userId);
        return ResponseEntity.ok("사용자 삭제 완료");
    }
}
