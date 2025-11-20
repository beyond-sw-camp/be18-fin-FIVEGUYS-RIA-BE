package com.fiveguys.RIA.RIA_Backend.user.model.service.impl;


import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthErrorCode;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.util.JwtUtil;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.RefreshResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.service.RedisTokenService;
import com.fiveguys.RIA.RIA_Backend.user.model.service.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    @Override
    @Transactional
    public RefreshResponseDto reissue(String refreshToken) {

        // 1. 유효성 검사
        jwtUtil.validateToken(refreshToken);
        if (!"refresh".equals(jwtUtil.getCategory(refreshToken))) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. 사용자 식별자 확인
        String username = jwtUtil.getEmployeeNo(refreshToken);
        String savedRefresh = redisTokenService.getRefreshToken(username);
        if (savedRefresh == null || !savedRefresh.equals(refreshToken)) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 3. 새 토큰 발급
        String role = jwtUtil.getRole(refreshToken);
        String newAccess = jwtUtil.createAccessToken(username, role, null);
        String newRefresh = jwtUtil.createRefreshToken(username);

        // 4. Redis 갱신
        redisTokenService.deleteRefreshToken(username);
        long refreshTtl = (jwtUtil.getExpiration(newRefresh).getTime() - System.currentTimeMillis()) / 1000;
        redisTokenService.saveRefreshToken(username, newRefresh, refreshTtl);

        // 5. 결과 반환
        return RefreshResponseDto.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .message("토큰 재발급 성공")
                .build();
    }
}