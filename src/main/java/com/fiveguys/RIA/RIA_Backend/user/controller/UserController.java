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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "사용자 인증/프로필/프로젝트 API")

public class UserController {

    private final TokenResponseWriter tokenResponseWriter;
    private final RefreshService refreshService;
    private final LogoutService logoutService;
    private final PasswordService passwordService;
    private final UserService userService;

    @PostMapping("/refresh")
    @Operation(
        summary = "액세스 토큰 재발급",
        description = "쿠키에 저장된 refresh_token을 사용하여 새로운 액세스/리프레시 토큰을 발급한다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");
        RefreshResponseDto dto = refreshService.reissue(refreshToken);
        tokenResponseWriter.writeTokens(response, dto);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/logout")
    @Operation(
        summary = "로그아웃",
        description = "리프레시 토큰을 무효화하고 관련 쿠키를 제거한다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그아웃 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok(Map.of("message", "로그아웃이 정상적으로 처리되었습니다."));
    }

    @PatchMapping("/password")
    @Operation(
        summary = "비밀번호 변경",
        description = "기존 비밀번호를 검증한 뒤 새 비밀번호로 변경한다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
        @ApiResponse(responseCode = "401", description = "인증 실패 / 기존 비밀번호 불일치", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody PasswordChangeRequestDto dto) {
        passwordService.changePassword(dto);
        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }

    @GetMapping("/profile")
    @Operation(
        summary = "내 프로필 조회",
        description = "현재 로그인한 사용자의 프로필 상세 정보를 조회한다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ProfileResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ProfileResponseDto getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getProfile(userDetails.getUserId());
    }

    @GetMapping("/projects/active")
    @Operation(
        summary = "내 진행 중 프로젝트 목록 조회",
        description = "현재 로그인한 사용자가 참여 중인 진행 상태의 프로젝트 목록을 페이징 조회한다."
    )
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1"),
        @Parameter(name = "size", description = "페이지 크기", example = "10")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = MyProjectResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<List<MyProjectResponseDto>> getActiveProjects(
        @AuthenticationPrincipal(expression = "userId") Long userId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<MyProjectResponseDto> response = userService.getActiveProjects(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    @Operation(
        summary = "사용자 간단 목록 조회",
        description = "사내 사용자 목록을 간단 정보(이름, 사번 등) 기준으로 조회한다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserSimpleResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public List<UserSimpleResponseDto> getUserList() {
        return userService.getUserList();
    }
}
