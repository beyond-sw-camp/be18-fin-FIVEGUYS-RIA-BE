package com.fiveguys.RIA.RIA_Backend.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiveguys.RIA.RIA_Backend.auth.handler.CustomFailureHandler;
import com.fiveguys.RIA.RIA_Backend.auth.handler.CustomSuccessHandler;
import com.fiveguys.RIA.RIA_Backend.auth.token.UserAuthenticationToken;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.request.LoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public LoginFilter(AuthenticationManager authenticationManager,
                       CustomSuccessHandler successHandler,
                       CustomFailureHandler failureHandler) {
        super(authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info(">>> [STEP 1] LoginFilter 진입: method={}, uri={}",
                request.getMethod(), request.getRequestURI());
        System.out.println(new BCryptPasswordEncoder().encode("1234"));


        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            log.warn(">>> [STEP 1-1] 잘못된 요청 메서드: {} (POST만 허용됨)", request.getMethod());
            return null;
        }

        try {
            String contentType = request.getContentType();
            log.info(">>> [STEP 2] Content-Type: {}", contentType);

            ObjectMapper mapper = new ObjectMapper();
            LoginRequestDto loginDto = mapper.readValue(request.getInputStream(), LoginRequestDto.class);

            if (loginDto == null) {
                log.warn(">>> [STEP 2-1] 요청 Body 파싱 실패 (loginDto가 null)");
                throw new RuntimeException("요청 Body 파싱 실패");
            }

            log.info(">>> [STEP 3] 로그인 요청 수신: employeeNo={}, password(length)={}",
                    loginDto.getEmployeeNo(),
                    loginDto.getPassword() != null ? loginDto.getPassword().length() : 0);

            UserAuthenticationToken authToken =
                    new UserAuthenticationToken(loginDto.getEmployeeNo(), loginDto.getPassword());

            log.info(">>> [STEP 4] AuthenticationManager 호출: {}", this.getAuthenticationManager());
            Authentication authentication = this.getAuthenticationManager().authenticate(authToken);

            log.info(">>> [STEP 5] Authentication 결과: {}", authentication != null ? "성공" : "실패(null)");
            return authentication;

        } catch (IOException e) {
            log.error(">>> [ERROR] 로그인 요청 Body 읽기 실패", e);
            throw new RuntimeException("로그인 요청 처리 중 오류 발생", e);
        } catch (Exception e) {
            log.error(">>> [ERROR] 로그인 처리 중 예외", e);
            throw e;
        }
    }
}
