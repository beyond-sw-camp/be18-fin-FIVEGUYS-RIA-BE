package com.fiveguys.RIA.RIA_Backend.user.model.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RefreshResponseDto {
    private String accessToken;
    private String refreshToken;
    private String message;
}
