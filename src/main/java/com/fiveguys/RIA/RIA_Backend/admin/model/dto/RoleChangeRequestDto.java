package com.fiveguys.RIA.RIA_Backend.admin.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleChangeRequestDto {
    @NotNull(message = "권한 ID는 필수입니다.")
    private Long roleId;
}

