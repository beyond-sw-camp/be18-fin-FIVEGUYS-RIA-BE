package com.fiveguys.RIA.RIA_Backend.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordChangeRequestDto {

    @NotBlank(message = "기존 비밀번호는 필수 입력값입니다.")
    private String oldPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력값입니다.")
    private String newPassword;
}