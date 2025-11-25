package com.fiveguys.RIA.RIA_Backend.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.AdminLogRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminLogService;
import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthErrorCode;
import com.fiveguys.RIA.RIA_Backend.common.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper om = new ObjectMapper();
    private final AdminLogService adminLogService;

    public CustomFailureHandler(AdminLogService adminLogService) {
        this.adminLogService = adminLogService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        AuthErrorCode errorCode = AuthErrorCode.INVALID_LOGIN;

        // 로그인 실패 로그 저장
        AdminLogRequestDto logDto = AdminLogRequestDto.builder()
                                                      .actorId(null)
                                                      .logName("Auth.login")
                                                      .resource(request.getMethod() + " " + request.getRequestURI())
                                                      .state("FAIL")
                                                      .build();
        adminLogService.save(logDto);

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse body = ErrorResponse.of(errorCode);
        om.writeValue(response.getWriter(), body);

        System.out.println(">>> 로그인 실패: " + errorCode.getMessage());
    }
}

