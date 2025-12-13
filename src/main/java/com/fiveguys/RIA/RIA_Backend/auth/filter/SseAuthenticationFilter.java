package com.fiveguys.RIA.RIA_Backend.auth.filter;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.JwtUserDetailsLoader;
import com.fiveguys.RIA.RIA_Backend.auth.token.UserAuthenticationToken;
import com.fiveguys.RIA.RIA_Backend.common.util.JwtUtil;
import com.fiveguys.RIA.RIA_Backend.user.model.service.impl.RedisTokenServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
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
    private final RedisTokenServiceImpl redisTokenServiceImpl;
    private final JwtUserDetailsLoader jwtUserDetailsLoader;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        return acceptHeader == null || !acceptHeader.contains("text/event-stream");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = request.getParameter("token");

            if (token == null || token.isBlank()) {
                log.warn("[SSE] token 파라미터 없음");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            jwtUtil.validateToken(token);

            String jti = jwtUtil.getJti(token);
            if (redisTokenServiceImpl.isBlacklisted(jti)) {
                log.warn("[SSE] 블랙리스트 토큰");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            if (!"access".equals(jwtUtil.getCategory(token))) {
                log.warn("[SSE] access 토큰 아님");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            String employeeNo = jwtUtil.getEmployeeNo(token);
            CustomUserDetails userDetails =
                    jwtUserDetailsLoader.loadByEmployeeNo(employeeNo);

            Authentication authentication =
                    new UserAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("[SSE] 인증 성공 → userId={}", userDetails.getUserId());

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.info("[SSE] token expired - connection rejected (normal)");
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        } catch (Exception e) {
            log.error("[SSE] 인증 실패: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
