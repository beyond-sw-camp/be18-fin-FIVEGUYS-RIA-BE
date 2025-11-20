package com.fiveguys.RIA.RIA_Backend.auth.handler;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.AdminLogRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminLogService;
import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.common.util.JwtUtil;
import com.fiveguys.RIA.RIA_Backend.common.util.CookieUtil;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.service.impl.RedisTokenServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;
  private final RedisTokenServiceImpl redisTokenServiceImpl;
  private final AdminLogService adminLogService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication)
      throws IOException, ServletException {

    if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
      throw new IllegalStateException(
          "Unknown principal type: " + authentication.getPrincipal().getClass());
    }

    User user = userDetails.getUser();
    String employeeNo = user.getEmployeeNo();
    String role = user.getRole().getRoleName().name();

    //  Access / Refresh Token 발급 (상태 관계없이)
    String accessToken = jwtUtil.createAccessToken(
        employeeNo,
        role,
        user.getDepartment().name()// ← 이거 한 줄 추가로 해결

    );
    String refreshToken = jwtUtil.createRefreshToken(employeeNo);

    //  Redis 갱신
    redisTokenServiceImpl.deleteRefreshToken(employeeNo);
    Date refreshExp = jwtUtil.getExpiration(refreshToken);
    long refreshTtl = (refreshExp.getTime() - System.currentTimeMillis()) / 1000;
    redisTokenServiceImpl.saveRefreshToken(employeeNo, refreshToken, refreshTtl);

    // 로그인 성공 로그저장
    AdminLogRequestDto logDto = AdminLogRequestDto.builder()
                                                  .actorId(user.getId())                                 // 로그인한 사용자
                                                  .logName("Auth.login")                                 // 작업명
                                                  .resource(request.getMethod() + " " + request.getRequestURI()) // "POST /api/auth/login"
                                                  .state("SUCCESS")
                                                  .build();
    adminLogService.save(logDto);

    //  헤더 / 쿠키 저장
    response.setHeader("Authorization", "Bearer " + accessToken);
    response.addCookie(
        CookieUtil.createHttpOnlyCookie("refresh_token", refreshToken, (int) refreshTtl));

    //  TEMP_PASSWORD 여부에 따라 분기
    response.setStatus(HttpStatus.OK.value());
    response.setContentType("application/json;charset=UTF-8");

    if (user.getStatus() == User.Status.TEMP_PASSWORD) {
      log.info("임시 비밀번호 로그인: {}", employeeNo);
      response.getWriter().write("""
              {
                "requirePasswordChange": true,
                "message": "임시 비밀번호로 로그인했습니다. 비밀번호를 변경해주세요."
              }
          """);
    } else {
      log.info("로그인 성공: {}, ROLE: {}", employeeNo, role);
      response.getWriter().write("""
              {
                "requirePasswordChange": false,
                "message": "로그인 성공"
              }
          """);
    }
  }
}
