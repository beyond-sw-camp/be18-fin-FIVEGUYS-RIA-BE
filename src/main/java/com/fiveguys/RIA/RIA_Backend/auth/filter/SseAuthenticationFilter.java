package com.fiveguys.RIA.RIA_Backend.auth.filter;

import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthException;
import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.JwtUserDetailsLoader;
import com.fiveguys.RIA.RIA_Backend.auth.token.UserAuthenticationToken;
import com.fiveguys.RIA.RIA_Backend.common.util.JwtUtil;
import com.fiveguys.RIA.RIA_Backend.user.model.service.impl.RedisTokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailsLoader jwtUserDetailsLoader;
    private final RedisTokenServiceImpl redisTokenServiceImpl;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        return acceptHeader == null || !acceptHeader.contains("text/event-stream");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.warn("[SSE] Authorization 헤더 없음 또는 형식 오류");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = authorization.substring(7);

        try {
            // 1. 토큰 유효성 검사 (유효하지 않으면 예외 발생)
            jwtUtil.validateToken(token);
            log.info("[SSE] 토큰 유효성 검증 완료");

            // 2. 블랙리스트 확인
            String jti = jwtUtil.getJti(token);
            if (redisTokenServiceImpl.isBlacklisted(jti)) {
                log.warn("[SSE] 블랙리스트 토큰 감지 (jti={})", jti);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // 3. access 토큰 확인
            if (!"access".equals(jwtUtil.getCategory(token))) {
                log.warn("[SSE] access 토큰 아님");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // 4. UserDetails 생성
            String employeeNo = jwtUtil.getEmployeeNo(token);
            CustomUserDetails userDetails = jwtUserDetailsLoader.loadByEmployeeNo(employeeNo);

            // 5. Authentication 생성 후 SecurityContext 세팅
            Authentication authentication =
                    new UserAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("[SSE] SecurityContext 세팅 완료 → userId={}", userDetails.getUserId());

            // 6. 다음 필터 실행
            filterChain.doFilter(request, response);

        } catch (AuthException e) {
            log.warn("[SSE] 인증 실패: {} - {}", e.getErrorCode(), e.getErrorCode().getMessage());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            log.error("[SSE] 알 수 없는 오류: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
