package com.fiveguys.RIA.RIA_Backend.user.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.user.model.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_PREFIX = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    // ================== 🔹 Refresh Token ==================
    @Override
    public void saveRefreshToken(String employeeNo, String refreshToken, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + employeeNo,
                refreshToken,
                ttlSeconds,
                TimeUnit.SECONDS
        );
    }

    @Override
    public String getRefreshToken(String employeeNo) {
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + employeeNo);
    }

    @Override
    public void deleteRefreshToken(String employeeNo) {
        redisTemplate.delete(REFRESH_PREFIX + employeeNo);
    }

    // ================== 🔹 Blacklist ==================
    @Override
    public void blacklistAccessToken(String jti, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + jti,
                "logout",
                ttlSeconds,
                TimeUnit.SECONDS
        );
    }

    @Override
    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }
}
