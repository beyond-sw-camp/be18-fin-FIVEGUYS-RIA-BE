package com.fiveguys.RIA.RIA_Backend.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.warn(" 인증 실패: {}", authException.getMessage());

        AuthErrorCode errorCode;
        if (authException instanceof AuthException ae) {
            errorCode = ae.getErrorCode();
        } else {
            errorCode = AuthErrorCode.UNAUTHORIZED;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = Map.of(
                "code", errorCode.getCode(),
                "message", errorCode.getMessage(),
                "status", 401
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
