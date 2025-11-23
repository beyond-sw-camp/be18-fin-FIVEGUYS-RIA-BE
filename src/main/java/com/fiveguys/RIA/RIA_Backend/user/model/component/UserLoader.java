package com.fiveguys.RIA.RIA_Backend.user.model.component;

import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthErrorCode;
import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthException;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLoader {

  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;

  public User loadUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
  }

  public List<Project> loadActiveProjects(Long userId, Pageable pageable) {
    List<Project> projects = projectRepository.findActiveProjectsByUserId(userId, pageable);
    if (projects.isEmpty()) {
      throw new CustomException(ProjectErrorCode.NO_ACTIVE_PROJECTS);
    }
    return projects;
  }
  public List<User> loadAllNonAdminUsers() {
    return userRepository.findAll().stream()
        .filter(user -> !"ROLE_ADMIN".equals(user.getRole().getRoleName()))
        .toList();
  }
}