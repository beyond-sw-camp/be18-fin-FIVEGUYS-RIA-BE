package com.fiveguys.RIA.RIA_Backend.auth.filter;

import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthErrorCode;
import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthException;
import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.JwtUserDetailsLoader;
import com.fiveguys.RIA.RIA_Backend.auth.token.UserAuthenticationToken;
import com.fiveguys.RIA.RIA_Backend.common.util.JwtUtil;
import com.fiveguys.RIA.RIA_Backend.user.model.service.impl.RedisTokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

  @Mock
  JwtUtil jwtUtil;

  @Mock
  JwtUserDetailsLoader jwtUserDetailsLoader;

  @Mock
  RedisTokenServiceImpl redisTokenServiceImpl;

  @Mock
  AuthenticationEntryPoint authenticationEntryPoint;

  @Mock
  FilterChain filterChain;

  @Mock
  CustomUserDetails customUserDetails;

  @InjectMocks
  JwtFilter jwtFilter;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  // ================== shouldNotFilter ==================

  @Test
  void login_path는_필터_스킵() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
    MockHttpServletResponse response = new MockHttpServletResponse();

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtUtil, jwtUserDetailsLoader, redisTokenServiceImpl, authenticationEntryPoint);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  void refresh_path는_필터_스킵() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/users/refresh");
    MockHttpServletResponse response = new MockHttpServletResponse();

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtUtil, jwtUserDetailsLoader, redisTokenServiceImpl, authenticationEntryPoint);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  void health_path는_필터_스킵() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/health");
    MockHttpServletResponse response = new MockHttpServletResponse();

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtUtil, jwtUserDetailsLoader, redisTokenServiceImpl, authenticationEntryPoint);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  // ================== Authorization 헤더 분기 ==================

  @Test
  void Authorization_없으면_체인만_통과() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    MockHttpServletResponse response = new MockHttpServletResponse();

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtUtil, jwtUserDetailsLoader, redisTokenServiceImpl, authenticationEntryPoint);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  void Authorization_형식이_Bearer가_아니면_체인만_통과() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    request.addHeader("Authorization", "Token abc");
    MockHttpServletResponse response = new MockHttpServletResponse();

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtUtil, jwtUserDetailsLoader, redisTokenServiceImpl, authenticationEntryPoint);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  // ================== 정상 토큰 ==================

  @Test
  void 유효한_access_token이면_SecurityContext에_Authentication_저장() throws Exception {
    String token = "valid-token";
    String jti = "jti-123";
    String employeeNo = "E001";
    String role = "ROLE_USER";

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    request.addHeader("Authorization", "Bearer " + token);
    MockHttpServletResponse response = new MockHttpServletResponse();

    doNothing().when(jwtUtil).validateToken(token);
    when(jwtUtil.getJti(token)).thenReturn(jti);
    when(redisTokenServiceImpl.isBlacklisted(jti)).thenReturn(false);
    when(jwtUtil.getCategory(token)).thenReturn("access");
    when(jwtUtil.getEmployeeNo(token)).thenReturn(employeeNo);
    when(jwtUtil.getRole(token)).thenReturn(role);

    when(customUserDetails.getAuthorities()).thenReturn(Collections.emptyList());
    when(jwtUserDetailsLoader.loadByEmployeeNo(employeeNo)).thenReturn(customUserDetails);

    jwtFilter.doFilter(request, response, filterChain);

    verify(jwtUtil).validateToken(token);
    verify(redisTokenServiceImpl).isBlacklisted(jti);
    verify(jwtUserDetailsLoader).loadByEmployeeNo(employeeNo);
    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(authenticationEntryPoint);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    assertThat(auth).isInstanceOf(UserAuthenticationToken.class);
    assertThat(auth.getPrincipal()).isEqualTo(customUserDetails);
    assertThat(auth.isAuthenticated()).isTrue();
  }

  // ================== 블랙리스트 ==================

  @Test
  void 블랙리스트_토큰이면_BLACKLISTED_ACCESS_TOKEN() throws Exception {
    String token = "black-token";
    String jti = "black-jti";

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    request.addHeader("Authorization", "Bearer " + token);
    MockHttpServletResponse response = new MockHttpServletResponse();

    doNothing().when(jwtUtil).validateToken(token);
    when(jwtUtil.getJti(token)).thenReturn(jti);
    when(redisTokenServiceImpl.isBlacklisted(jti)).thenReturn(true);

    jwtFilter.doFilter(request, response, filterChain);

    verify(jwtUtil).validateToken(token);
    verify(redisTokenServiceImpl).isBlacklisted(jti);
    verify(jwtUserDetailsLoader, never()).loadByEmployeeNo(anyString());
    verify(filterChain, never()).doFilter(request, response);

    verify(authenticationEntryPoint).commence(
        eq(request),
        eq(response),
        argThat(authExceptionMatches(AuthErrorCode.BLACKLISTED_ACCESS_TOKEN))
    );

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  // ================== 잘못된 카테고리 ==================

  @Test
  void access_아닌_카테고리면_INVALID_TOKEN_CATEGORY() throws Exception {
    String token = "refresh-token";
    String jti = "jti-refresh";

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    request.addHeader("Authorization", "Bearer " + token);
    MockHttpServletResponse response = new MockHttpServletResponse();

    doNothing().when(jwtUtil).validateToken(token);
    when(jwtUtil.getJti(token)).thenReturn(jti);
    when(redisTokenServiceImpl.isBlacklisted(jti)).thenReturn(false);
    when(jwtUtil.getCategory(token)).thenReturn("refresh");

    jwtFilter.doFilter(request, response, filterChain);

    verify(jwtUtil).validateToken(token);
    verify(redisTokenServiceImpl).isBlacklisted(jti);
    verify(jwtUserDetailsLoader, never()).loadByEmployeeNo(anyString());
    verify(filterChain, never()).doFilter(request, response);

    verify(authenticationEntryPoint).commence(
        eq(request),
        eq(response),
        argThat(authExceptionMatches(AuthErrorCode.INVALID_TOKEN_CATEGORY))
    );

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  // ================== 만료 토큰 ==================

  @Test
  void 만료된_토큰이면_ACCESS_TOKEN_EXPIRED() throws Exception {
    String token = "expired-token";

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    request.addHeader("Authorization", "Bearer " + token);
    MockHttpServletResponse response = new MockHttpServletResponse();

    io.jsonwebtoken.ExpiredJwtException expired = mock(io.jsonwebtoken.ExpiredJwtException.class);
    doThrow(expired).when(jwtUtil).validateToken(token);

    jwtFilter.doFilter(request, response, filterChain);

    verify(jwtUtil).validateToken(token);
    verify(redisTokenServiceImpl, never()).isBlacklisted(anyString());
    verify(jwtUserDetailsLoader, never()).loadByEmployeeNo(anyString());
    verify(filterChain, never()).doFilter(request, response);

    verify(authenticationEntryPoint).commence(
        eq(request),
        eq(response),
        argThat(authExceptionMatches(AuthErrorCode.ACCESS_TOKEN_EXPIRED))
    );

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  // ================== 기타 예외 ==================

  @Test
  void 예상치_못한_예외발생시_UNAUTHORIZED() throws Exception {
    String token = "weird-token";
    String jti = "jti-weird";

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    request.addHeader("Authorization", "Bearer " + token);
    MockHttpServletResponse response = new MockHttpServletResponse();

    doNothing().when(jwtUtil).validateToken(token);
    when(jwtUtil.getJti(token)).thenReturn(jti);
    when(redisTokenServiceImpl.isBlacklisted(jti)).thenReturn(false);
    when(jwtUtil.getCategory(token)).thenReturn("access");
    when(jwtUtil.getEmployeeNo(token)).thenReturn("E001");
    when(jwtUserDetailsLoader.loadByEmployeeNo("E001"))
        .thenThrow(new RuntimeException("DB error"));

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain, never()).doFilter(request, response);

    verify(authenticationEntryPoint).commence(
        eq(request),
        eq(response),
        argThat(authExceptionMatches(AuthErrorCode.UNAUTHORIZED))
    );

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  // ================== 헬퍼 ==================

  private org.mockito.ArgumentMatcher<AuthenticationException> authExceptionMatches(AuthErrorCode expected) {
    return ex -> {
      if (!(ex instanceof AuthException)) return false;
      AuthException ae = (AuthException) ex;
      return ae.getErrorCode() == expected;
    };
  }
}
