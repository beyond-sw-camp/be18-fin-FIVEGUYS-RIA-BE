package com.fiveguys.RIA.RIA_Backend.user.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.common.util.CookieUtil;
import com.fiveguys.RIA.RIA_Backend.common.util.TokenResponseWriter;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.request.PasswordChangeRequestDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.MyProjectResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.ProfileResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.RefreshResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.UserSimpleResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.service.LogoutService;
import com.fiveguys.RIA.RIA_Backend.user.model.service.PasswordService;
import com.fiveguys.RIA.RIA_Backend.user.model.service.RefreshService;
import com.fiveguys.RIA.RIA_Backend.user.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final TokenResponseWriter tokenResponseWriter;
    private final RefreshService refreshService;
    private final LogoutService logoutService;
    private final PasswordService passwordService;
    private final UserService userService;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");
        RefreshResponseDto dto = refreshService.reissue(refreshToken);
        tokenResponseWriter.writeTokens(response, dto);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok(Map.of("message", "로그아웃이 정상적으로 처리되었습니다."));
    }

    @PatchMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody PasswordChangeRequestDto dto) {
        passwordService.changePassword(dto);
        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }

    @GetMapping("/profile")
    public ProfileResponseDto getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getProfile(userDetails.getUserId());
    }

    @GetMapping("/projects/active")
    public ResponseEntity<List<MyProjectResponseDto>> getActiveProjects(
        @AuthenticationPrincipal(expression = "userId") Long userId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<MyProjectResponseDto> response = userService.getActiveProjects(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public List<UserSimpleResponseDto> getUserList() {
        return userService.getUserList();
    }
}
