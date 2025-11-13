package com.fiveguys.RIA.RIA_Backend.user.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthErrorCode;
import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.MyProjectResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.ProfileResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
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
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;


  @Override
  public ProfileResponseDto getProfile(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() ->
        new AuthException(AuthErrorCode.USER_NOT_FOUND));

    return ProfileResponseDto.builder()
        .department(user.getDepartment().name())
        .email(user.getEmail())
        .employeeId(user.getEmployeeNo())
        .name(user.getName())
        .position(user.getPosition())
        .build();
  }

  @Override
  public List<MyProjectResponseDto> getActiveProjects(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    List<Project> projects = projectRepository.findActiveProjectsByUserId(userId, pageable);

    if (projects.isEmpty()) {
      throw new CustomException(ProjectErrorCode.NO_ACTIVE_PROJECTS);
    }

    return projects.stream()
        .map(project -> {
          double marginRate = project.getExpectedMarginRate() != null
              ? project.getExpectedMarginRate().doubleValue()
              : 0.0;
          int expectedRevenue = project.getExpectedRevenue() != null
              ? project.getExpectedRevenue()
              : 0;
          int expectedProfit = (int) (expectedRevenue * (marginRate / 100));

          return MyProjectResponseDto.builder()
              .projectId(project.getProjectId())
              .title(project.getTitle())
              .clientCompanyName(project.getClientCompany().getCompanyName())
              .startDay(project.getStartDay())
              .endDay(project.getEndDay())
              .expectedRevenue(expectedRevenue)
              .expectedMarginRate(marginRate)
              .expectedProfit(expectedProfit)
              .salesManagerName(project.getSalesManager().getName())
              .status(project.getStatus().name())
              .build();
        })
        .collect(Collectors.toList());
  }
}
