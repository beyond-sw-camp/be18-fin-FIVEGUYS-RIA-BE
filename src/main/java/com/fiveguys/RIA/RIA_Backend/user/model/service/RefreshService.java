package com.fiveguys.RIA.RIA_Backend.user.model.service;


import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.RefreshResponseDto;

public interface RefreshService {

    /**
     * Refresh Token으로 새로운 Access/Refresh Token을 발급한다.
     *
     * @param refreshToken 클라이언트가 가진 Refresh Token
     * @return 새로 발급된 Access/Refresh Token DTO
     */
    RefreshResponseDto reissue(String refreshToken);
}
