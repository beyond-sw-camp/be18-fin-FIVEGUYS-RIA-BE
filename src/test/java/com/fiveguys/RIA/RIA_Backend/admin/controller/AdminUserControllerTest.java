package com.fiveguys.RIA.RIA_Backend.admin.controller;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.RoleChangeRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.AdminLogResponseDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.PageResponse;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.UserResponseDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminLogService;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminUserService;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * AdminUserController 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private AdminUserService adminUserService;

    @Mock
    private AdminLogService adminLogService;

    @InjectMocks
    private AdminUserController adminUserController;

    @Test
    @DisplayName("createUser: 사용자 생성 성공 시 사번 포함 메시지와 200 응답 반환")
    void createUser_success() {
        // given
        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                                                       .employeeNo("E001")
                                                       .name("홍길동")
                                                       .password("password123")
                                                       .roleId(1L)
                                                       .email("test@example.com")
                                                       .department("SALES")
                                                       .position("매니저")
                                                       .build();

        User createdUser = mock(User.class);
        given(createdUser.getEmployeeNo()).willReturn("E001");
        given(adminUserService.createUser(dto)).willReturn(createdUser);

        // when
        ResponseEntity<?> result = adminUserController.createUser(dto);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo("사용자 생성 완료: E001");

        verify(adminUserService).createUser(dto);
    }

    @Test
    @DisplayName("getRoles: 전체 역할 목록 조회 성공 시 200 응답과 리스트 반환")
    void getRoles_success() {
        // given
        Map<String, Object> role1 = Map.of(
                "roleId", 1L,
                "roleName", "ADMIN"
        );
        Map<String, Object> role2 = Map.of(
                "roleId", 2L,
                "roleName", "USER"
        );

        List<Map<String, Object>> roles = List.of(role1, role2);

        given(adminUserService.getRoles()).willReturn(roles);

        // when
        ResponseEntity<List<Map<String, Object>>> result = adminUserController.getRoles();

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).containsExactly(role1, role2);

        verify(adminUserService).getRoles();
    }

    @Test
    @DisplayName("changeUserRole: 권한 변경 요청 시 서비스 호출 및 200 응답")
    void changeUserRole_success() {
        // given
        Long userId = 10L;
        RoleChangeRequestDto dto = RoleChangeRequestDto.builder()
                                                       .roleId(3L)
                                                       .build();

        // when
        ResponseEntity<?> result = adminUserController.changeUserRole(userId, dto);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo("권한 변경 완료");

        verify(adminUserService).changeUserRole(userId, dto.getRoleId());
    }

    @Test
    @DisplayName("getLogs: 관리자 로그 페이지 조회 성공")
    void getLogs_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        AdminLogResponseDto logDto = AdminLogResponseDto.builder()
                                                        .logId(1L)
                                                        .actorId(100L)
                                                        .userName("관리자")
                                                        .employeeNo("E001")
                                                        .logName("USER_DELETE")
                                                        .resource("USER")
                                                        .state("SUCCESS")
                                                        .createdAt(LocalDateTime.now())
                                                        .build();

        List<AdminLogResponseDto> content = List.of(logDto);
        Page<AdminLogResponseDto> page = new PageImpl<>(content, pageable, content.size());

        given(adminLogService.getLogs(pageable)).willReturn(page);

        // when
        ResponseEntity<PageResponse<AdminLogResponseDto>> result =
                adminUserController.getLogs(pageable);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        PageResponse<AdminLogResponseDto> body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getContent()).containsExactly(logDto);
        assertThat(body.getPage()).isEqualTo(pageable.getPageNumber());
        assertThat(body.getSize()).isEqualTo(pageable.getPageSize());
        assertThat(body.getTotalElements()).isEqualTo(1L);
        assertThat(body.getTotalPages()).isEqualTo(1);
        assertThat(body.isFirst()).isTrue();
        assertThat(body.isLast()).isTrue();

        verify(adminLogService).getLogs(pageable);
    }


    @Test
    @DisplayName("getUsers: 사용자 목록 페이지 조회 성공")
    void getUsers_success() {
        // given
        Pageable pageable = PageRequest.of(1, 5);

        UserResponseDto user1 = UserResponseDto.builder()
                                               .id(1L)
                                               .employeeNo("E001")
                                               .name("유저1")
                                               .email("user1@example.com")
                                               .department("SALES")
                                               .position("매니저")
                                               .state("ACTIVE")
                                               .roleId(1L)
                                               .build();

        UserResponseDto user2 = UserResponseDto.builder()
                                               .id(2L)
                                               .employeeNo("E002")
                                               .name("유저2")
                                               .email("user2@example.com")
                                               .department("MARKETING")
                                               .position("사원")
                                               .state("ACTIVE")
                                               .roleId(2L)
                                               .build();

        List<UserResponseDto> content = List.of(user1, user2);

        // 🔥 totalElements = content.size() 로 수정
        Page<UserResponseDto> page = new PageImpl<>(content, pageable, content.size());

        given(adminUserService.getUsers(pageable)).willReturn(page);

        // when
        ResponseEntity<PageResponse<UserResponseDto>> result =
                adminUserController.getUsers(pageable);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        PageResponse<UserResponseDto> body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getContent()).containsExactly(user1, user2);
        assertThat(body.getPage()).isEqualTo(pageable.getPageNumber());
        assertThat(body.getSize()).isEqualTo(pageable.getPageSize());

        assertThat(body.getTotalElements()).isEqualTo(7L);

        assertThat(body.getTotalPages()).isEqualTo(2);
        assertThat(body.isFirst()).isFalse();
        assertThat(body.isLast()).isTrue();

        verify(adminUserService).getUsers(pageable);
    }


    @Test
    @DisplayName("deleteUser: 사용자 삭제 요청 시 서비스 호출 및 200 응답")
    void deleteUser_success() {
        // given
        Long userId = 5L;

        // when
        ResponseEntity<?> result = adminUserController.deleteUser(userId);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo("사용자 삭제 완료");

        verify(adminUserService).deleteUser(userId);
    }
}
