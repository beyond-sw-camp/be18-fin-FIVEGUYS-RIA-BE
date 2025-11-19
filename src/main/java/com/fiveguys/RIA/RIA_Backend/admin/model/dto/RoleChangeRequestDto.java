package com.fiveguys.RIA.RIA_Backend.admin.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleChangeRequestDto {
    @NotNull(message = "권한 ID는 필수입니다.")
    private Long roleId;
}

