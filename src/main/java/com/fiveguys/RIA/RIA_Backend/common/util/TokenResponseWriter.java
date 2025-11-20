package com.fiveguys.RIA.RIA_Backend.common.util;

import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.RefreshResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenResponseWriter {
    private final JwtUtil jwtUtil;

    public void writeTokens(HttpServletResponse response, RefreshResponseDto dto) {
        response.setHeader("Authorization", "Bearer " + dto.getAccessToken());
        int maxAge = (int)((jwtUtil.getExpiration(dto.getRefreshToken()).getTime() - System.currentTimeMillis()) / 1000L);

        Cookie cookie = CookieUtil.createHttpOnlyCookie(
                "refresh_token", dto.getRefreshToken(), maxAge
        );
        response.addCookie(cookie);
    }
}
