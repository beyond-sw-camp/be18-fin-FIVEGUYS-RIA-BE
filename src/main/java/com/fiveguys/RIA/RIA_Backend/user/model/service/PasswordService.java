package com.fiveguys.RIA.RIA_Backend.user.model.service;


import com.fiveguys.RIA.RIA_Backend.user.model.dto.request.PasswordChangeRequestDto;

public interface PasswordService {
    void changePassword(PasswordChangeRequestDto dto);
}