package com.fiveguys.RIA.RIA_Backend.common.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    // 만료시간: 분 단위
    private static final long ACCESS_TTL = 1000L * 60 * 30;     // 30분
    private static final long REFRESH_TTL = 1000L * 60 * 60 * 24 * 7; // 7일

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    // ================== Access Token ==================
    public String createAccessToken(String employeeNo, String role, String department) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claim("category", "access")
                .claim("employeeNo", employeeNo)
                .claim("role", role)
                .claim("department", department)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TTL))
                .signWith(secretKey)
                .compact();
    }

    // ================== Refresh Token ==================
    public String createRefreshToken(String employeeNo) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claim("category", "refresh")
                .claim("employeeNo", employeeNo)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TTL))
                .signWith(secretKey)
                .compact();
    }

    // ================== Claim 추출 ==================
    public String getEmployeeNo(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("employeeNo", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String getDepartment(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("department", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

    public String getJti(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    // ================== Expiration ==================
    public boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public Date getExpiration(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public void validateToken(String token) throws ExpiredJwtException {
        Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}