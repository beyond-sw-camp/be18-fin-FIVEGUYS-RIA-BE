package com.fiveguys.RIA.RIA_Backend.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestDto {

    @NotBlank(message = "사번은 필수 입력값입니다.")
    private String employeeNo;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
