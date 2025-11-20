package com.fiveguys.RIA.RIA_Backend.campaign.project.model.service;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectSearchRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectPipelineResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectTitleResponseDto;
import java.util.List;

public interface ProjectService {
  ProjectCreateResponseDto createProject(ProjectCreateRequestDto dto, Long userId);

  List<ProjectPipelineResponseDto> getProjectsWithPipelines(
      Long userId,
      ProjectSearchRequestDto request,
      int page,
      int size
  );

  ProjectDetailResponseDto getProjectDetail(Long projectId);
  ProjectDetailResponseDto updateProject(Long projectId, ProjectUpdateRequestDto dto, CustomUserDetails user);

  void deleteProject(Long projectId, CustomUserDetails user);

  List<ProjectTitleResponseDto> getProjectTitles(String keyword);

}

