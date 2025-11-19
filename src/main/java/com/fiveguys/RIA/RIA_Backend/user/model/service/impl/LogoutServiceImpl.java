package com.fiveguys.RIA.RIA_Backend.user.model.service.impl;


import com.fiveguys.RIA.RIA_Backend.common.util.JwtUtil;
import com.fiveguys.RIA.RIA_Backend.common.util.CookieUtil;
import com.fiveguys.RIA.RIA_Backend.user.model.service.LogoutService;
import com.fiveguys.RIA.RIA_Backend.user.model.service.RedisTokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 확인
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new JwtException("Authorization 헤더가 유효하지 않습니다.");
        }

        // Access Token 추출
        String accessToken = authorization.substring(7);

        // 토큰 만료 여부 확인
        if (jwtUtil.isExpired(accessToken)) {
            throw new JwtException("이미 만료된 Access Token입니다.");
        }

        // 사번(employeeNo) 추출
        String employeeNo = jwtUtil.getEmployeeNo(accessToken);

        // Redis에서 Refresh Token 삭제
        redisTokenService.deleteRefreshToken(employeeNo);

        // Refresh Token 쿠키 삭제
        response.addCookie(CookieUtil.deleteCookie("refresh_token"));

        // Access Token 블랙리스트 등록
        String jti = jwtUtil.getJti(accessToken);
        long ttl = (jwtUtil.getExpiration(accessToken).getTime() - System.currentTimeMillis()) / 1000;
        redisTokenService.blacklistAccessToken(jti, ttl);
    }
}