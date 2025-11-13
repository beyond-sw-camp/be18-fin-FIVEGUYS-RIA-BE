package com.fiveguys.RIA.RIA_Backend.user.model.service;

public interface RedisTokenService {

    // Refresh Token
    void saveRefreshToken(String employeeNo, String refreshToken, long ttlSeconds);

    String getRefreshToken(String employeeNo);

    void deleteRefreshToken(String employeeNo);

    // Access Token blacklist
    void blacklistAccessToken(String jti, long ttlSeconds);

    boolean isBlacklisted(String jti);
}