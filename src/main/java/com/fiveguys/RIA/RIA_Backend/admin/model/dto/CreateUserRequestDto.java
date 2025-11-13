package com.fiveguys.RIA.RIA_Backend.admin.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreateUserRequestDto {

    @NotBlank(message = "사번은 필수 입력값입니다.")
    private String employeeNo;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @NotNull(message = "권한 ID는 필수 입력값입니다.")
    private Long roleId;

    private String email;
    private String department;
    private String position;
}