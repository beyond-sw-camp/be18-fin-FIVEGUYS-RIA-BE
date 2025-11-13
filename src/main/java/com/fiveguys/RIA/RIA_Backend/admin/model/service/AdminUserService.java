package com.fiveguys.RIA.RIA_Backend.admin.model.service;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.CreateUserRequestDto;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;

public interface AdminUserService {
    User createUser(CreateUserRequestDto dto);

}