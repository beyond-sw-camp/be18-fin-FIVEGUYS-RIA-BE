package com.fiveguys.RIA.RIA_Backend.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder // 이게 없어서 테스트 코드 작성이 안되서 추가했음
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 이게 없어서 테스트 코드 작성이 안되서 추가했음
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordChangeRequestDto {

    @NotBlank(message = "기존 비밀번호는 필수 입력값입니다.")
    private String oldPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력값입니다.")
    private String newPassword;
}