package com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RoleChangeRequestDto {
    @NotNull(message = "권한 ID는 필수입니다.")
    private Long roleId;
}

