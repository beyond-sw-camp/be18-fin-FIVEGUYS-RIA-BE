package com.fiveguys.RIA.RIA_Backend.common.config;


import com.fiveguys.RIA.RIA_Backend.auth.exception.RestAccessDeniedHandler;
import com.fiveguys.RIA.RIA_Backend.auth.exception.RestAuthenticationEntryPoint;
import com.fiveguys.RIA.RIA_Backend.auth.filter.JwtFilter;
import com.fiveguys.RIA.RIA_Backend.auth.filter.LoginFilter;
import com.fiveguys.RIA.RIA_Backend.auth.handler.CustomFailureHandler;
import com.fiveguys.RIA.RIA_Backend.auth.handler.CustomSuccessHandler;
import com.fiveguys.RIA.RIA_Backend.auth.service.JwtUserDetailsLoader;
import com.fiveguys.RIA.RIA_Backend.common.util.JwtUtil;
import com.fiveguys.RIA.RIA_Backend.user.model.service.impl.RedisTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtUserDetailsLoader  jwtUserDetailsLoader;
    private final RedisTokenServiceImpl redisTokenServiceImpl;

    //  PasswordEncoder 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //  AuthenticationManager — 스프링 컨텍스트 기반으로 받아야 함
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //  DaoAuthenticationProvider Bean 등록 (권장)
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                );

        http.authorizeHttpRequests(auth -> auth

                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/actuator/health","/swagger-ui.html").permitAll()
                .requestMatchers("/api/auth/login", "/api/users/refresh").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/sales/**").hasAnyRole("SALES_LEAD", "SALES_MEMBER")
                .requestMatchers("/api/**").authenticated()



                // ✅ 캘린더 사용자 추가 및 삭제 권한 제한
                .requestMatchers(HttpMethod.POST, "/api/calendars/users")
                .hasAnyRole("ADMIN", "SALES_LEAD")
                .requestMatchers(HttpMethod.DELETE, "/api/calendars/users")
                .hasAnyRole("ADMIN", "SALES_LEAD")

                // 📌 캘린더 사용자 목록 조회는 제한 없도록
                .requestMatchers(HttpMethod.GET, "/api/calendars/users").authenticated()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
        );

        //  로그인 필터 등록
        LoginFilter loginFilter = new LoginFilter(authenticationManager, customSuccessHandler, customFailureHandler);
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        //  JWT 필터 등록 (로그인 필터보다 앞에 두면 토큰 인증이 먼저 작동)
        http.addFilterBefore(
                new JwtFilter(jwtUtil,jwtUserDetailsLoader, redisTokenServiceImpl, restAuthenticationEntryPoint),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    //  CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://localhost:3000",
            "https://ria-sales.site",
            "https://www.ria-sales.site"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
