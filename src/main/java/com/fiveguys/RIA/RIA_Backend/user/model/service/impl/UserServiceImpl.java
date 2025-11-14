package com.fiveguys.RIA.RIA_Backend.user.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.user.model.component.UserLoader;
import com.fiveguys.RIA.RIA_Backend.user.model.component.UserMapper;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.MyProjectResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.ProfileResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserLoader userLoader;
  private final UserMapper userMapper;

  @Override
  public ProfileResponseDto getProfile(Long userId) {
    User user = userLoader.loadUser(userId);
    return userMapper.toProfileDto(user);
  }

  @Override
  public List<MyProjectResponseDto> getActiveProjects(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

    return userLoader.loadActiveProjects(userId, pageable).stream()
        .map(userMapper::toMyProject)
        .collect(Collectors.toList());
  }
}
