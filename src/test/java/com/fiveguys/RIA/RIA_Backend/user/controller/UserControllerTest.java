package com.fiveguys.RIA.RIA_Backend.user.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.common.util.TokenResponseWriter;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.request.PasswordChangeRequestDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.MyProjectResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.ProfileResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.RefreshResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.UserSimpleResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.service.LogoutService;
import com.fiveguys.RIA.RIA_Backend.user.model.service.PasswordService;
import com.fiveguys.RIA.RIA_Backend.user.model.service.RefreshService;
import com.fiveguys.RIA.RIA_Backend.user.model.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private TokenResponseWriter tokenResponseWriter;

  @Mock
  private RefreshService refreshService;

  @Mock
  private LogoutService logoutService;

  @Mock
  private PasswordService passwordService;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @Test
  @DisplayName("refresh: 쿠키의 refresh_token 으로 토큰 재발급 성공")
  void refresh_success() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    String refreshToken = "dummy-refresh";
    Cookie cookie = new Cookie("refresh_token", refreshToken);
    request.setCookies(cookie);

    RefreshResponseDto dto = RefreshResponseDto.builder()
        .accessToken("new-access")
        .refreshToken("new-refresh")
        .message("ok")
        .build();

    given(refreshService.reissue(refreshToken)).willReturn(dto);

    // when
    ResponseEntity<Void> result = userController.refresh(request, response);

    // then
    assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    verify(refreshService).reissue(refreshToken);
    verify(tokenResponseWriter).writeTokens(response, dto);
  }

  @Test
  @DisplayName("logout: 로그아웃 서비스 호출 및 200 응답")
  void logout_success() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();

    // when
    ResponseEntity<Map<String, String>> result = userController.logout(request, response);

    // then
    assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(result.getBody())
        .isNotNull()
        .containsEntry("message", "로그아웃이 정상적으로 처리되었습니다.");

    verify(logoutService).logout(request, response);
  }

  @Test
  @DisplayName("changePassword: 비밀번호 변경 요청 시 서비스 호출 및 200 응답")
  void changePassword_success() {
    // given
    PasswordChangeRequestDto dto = PasswordChangeRequestDto.builder()
        .oldPassword("old-pass")
        .newPassword("new-pass")
        .build();

    // when
    ResponseEntity<Map<String, String>> result = userController.changePassword(dto);

    // then
    assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(result.getBody())
        .isNotNull()
        .containsEntry("message", "비밀번호가 성공적으로 변경되었습니다.");

    verify(passwordService).changePassword(dto);
  }

  @Test
  @DisplayName("getProfile: 인증 사용자 프로필 조회")
  void getProfile_success() {
    // given
    CustomUserDetails userDetails = mock(CustomUserDetails.class);
    Long userId = 1L;
    given(userDetails.getUserId()).willReturn(userId);

    ProfileResponseDto profile = ProfileResponseDto.builder()
        .userId(userId)
        .department("SALES")
        .email("test@example.com")
        .employeeId("E001")
        .name("홍길동")
        .position("매니저")
        .build();

    given(userService.getProfile(userId)).willReturn(profile);

    // when
    ProfileResponseDto result = userController.getProfile(userDetails);

    // then
    assertThat(result).isSameAs(profile);
    verify(userService).getProfile(userId);
  }

  @Test
  @DisplayName("getActiveProjects: 진행중 프로젝트 목록 조회")
  void getActiveProjects_success() {
    // given
    Long userId = 1L;
    int page = 2;
    int size = 5;

    MyProjectResponseDto project = MyProjectResponseDto.builder()
        .projectId(10L)
        .title("프로젝트A")
        .clientCompanyName("고객사")
        .startDay(LocalDate.of(2025, 1, 1))
        .endDay(LocalDate.of(2025, 12, 31))
        .type("RENTAL")
        .salesManagerName("영업1")
        .status("ACTIVE")
        .build();

    List<MyProjectResponseDto> list = List.of(project);

    // UserService 시그니처에 맞춰서 인자 사용
    given(userService.getActiveProjects(userId, page, size)).willReturn(list);

    // when
    ResponseEntity<List<MyProjectResponseDto>> result =
        userController.getActiveProjects(userId, page, size);

    // then
    assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(result.getBody()).containsExactly(project);
    verify(userService).getActiveProjects(userId, page, size);
  }

  @Test
  @DisplayName("getUserList: 전체 사용자 간단 정보 목록 조회")
  void getUserList_success() {
    // given
    UserSimpleResponseDto u1 = UserSimpleResponseDto.builder()
        .userId(1L)
        .name("유저1")
        .build();

    UserSimpleResponseDto u2 = UserSimpleResponseDto.builder()
        .userId(2L)
        .name("유저2")
        .build();

    List<UserSimpleResponseDto> list = List.of(u1, u2);

    given(userService.getUserList()).willReturn(list);

    // when
    List<UserSimpleResponseDto> result = userController.getUserList();

    // then
    assertThat(result).containsExactly(u1, u2);
    verify(userService).getUserList();
  }
}
