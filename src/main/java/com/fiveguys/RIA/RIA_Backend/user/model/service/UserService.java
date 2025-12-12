package com.fiveguys.RIA.RIA_Backend.user.model.service;

import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.MyProjectResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.ProfileResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.UserSimpleResponseDto;
import java.util.List;

public interface UserService {
  ProfileResponseDto getProfile(Long userId);

  List<MyProjectResponseDto> getActiveProjects(Long userId, int limit, int offset);

  List<UserSimpleResponseDto> getUserList();
}
