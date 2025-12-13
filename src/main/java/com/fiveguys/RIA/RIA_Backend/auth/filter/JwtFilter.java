package com.fiveguys.RIA.RIA_Backend.auth.filter;


import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthErrorCode;
import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthException;
import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.JwtUserDetailsLoader;
import com.fiveguys.RIA.RIA_Backend.auth.token.UserAuthenticationToken;
import com.fiveguys.RIA.RIA_Backend.common.util.JwtUtil;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.service.impl.RedisTokenServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailsLoader jwtUserDetailsLoader;
    private final RedisTokenServiceImpl redisTokenServiceImpl;
    private final AuthenticationEntryPoint entryPoint;

    /** 로그인, 회원가입, 토큰 재발급 헬스체크 같은 API는 필터 제외 */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean skip =
                path.startsWith("/api/auth/login") ||
                path.startsWith("/api/users/refresh") ||
                path.equals("/actuator/health") ||
                path.startsWith("/debug/sales")||
                path.startsWith("/debug/settlement") ||
                path.startsWith("/api/sse"); // sse
        if (skip) {
            log.info(" JwtFilter skip: {}", path);
        }
        return skip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        log.info(" [JWT FILTER] 진입 → path={}, Authorization={}", request.getRequestURI(), authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.warn(" Authorization 헤더 없음 또는 형식 오류 (path={})", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {
            log.info(" STEP1: 토큰 유효성 검사 시작");
            jwtUtil.validateToken(token);
            log.info(" STEP1: 토큰 유효성 검사 통과");

            String jti = jwtUtil.getJti(token);
            log.info(" STEP2: 블랙리스트 확인 (jti={})", jti);
            if (redisTokenServiceImpl.isBlacklisted(jti)) {
                log.warn(" STEP2 실패: 블랙리스트 토큰 감지");
                throw new AuthException(AuthErrorCode.BLACKLISTED_ACCESS_TOKEN);
            }
            log.info(" STEP2 통과: 블랙리스트 아님");

            String category = jwtUtil.getCategory(token);
            log.info(" STEP3: 토큰 카테고리 확인 = {}", category);
            if (!"access".equals(category)) {
                log.warn(" STEP3 실패: access 토큰 아님");
                throw new AuthException(AuthErrorCode.INVALID_TOKEN_CATEGORY);
            }

            String employeeNo = jwtUtil.getEmployeeNo(token);
            String role = jwtUtil.getRole(token);
            log.info(" STEP4: 클레임 추출 완료 → employeeNo={}, role={}", employeeNo, role);

            log.info(" STEP5: DB 조회 및 UserDetails 생성 (employeeNo={})", employeeNo);
            CustomUserDetails userDetails = jwtUserDetailsLoader.loadByEmployeeNo(employeeNo);

            Authentication auth =
                new UserAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info(" STEP6: SecurityContext 저장 완료 → authorities={}", userDetails.getAuthorities());

            filterChain.doFilter(request, response);
            log.info(" STEP7: 다음 필터로 전달 완료");

        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            log.warn(" [ExpiredJwtException] Access Token 만료", e);
            entryPoint.commence(request, response,
                    new AuthException(AuthErrorCode.ACCESS_TOKEN_EXPIRED));

        } catch (AuthException e) {
            SecurityContextHolder.clearContext();
            log.warn(" [AuthException] {} ({})", e.getErrorCode(), e.getErrorCode().getMessage());
            entryPoint.commence(request, response, e);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error(" [Unexpected Exception] {}", e.getMessage(), e);
            entryPoint.commence(request, response,
                    new AuthException(AuthErrorCode.UNAUTHORIZED));
        }
    }
}
