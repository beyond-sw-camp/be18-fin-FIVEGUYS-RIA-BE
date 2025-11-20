package com.fiveguys.RIA.RIA_Backend.campaign.project.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectSearchRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectMetaResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectPipelineResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectTitleResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.service.ProjectService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

  private final ProjectService projectService;

  //프로젝트 생성
  @PostMapping
  public ResponseEntity<ProjectCreateResponseDto> createProject(
      @RequestBody ProjectCreateRequestDto dto,
      @AuthenticationPrincipal(expression = "userId") Long userId) {

    ProjectCreateResponseDto response = projectService.createProject(dto, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  //프로젝트 목록 조회
  @GetMapping("/pipelines")
  public ResponseEntity<List<ProjectPipelineResponseDto>> getProjectsWithPipelines(
      @AuthenticationPrincipal(expression = "userId") Long userId,
      ProjectSearchRequestDto request,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int size
  ) {
    List<ProjectPipelineResponseDto> result =
        projectService.getProjectsWithPipelines(userId, request, page, size);
    return ResponseEntity.ok(result);
  }

  //프로젝트 상세조회
  @GetMapping("/{projectId}")
  public ProjectDetailResponseDto getProjectDetail(@PathVariable Long projectId) {
    return projectService.getProjectDetail(projectId);
  }

  //프로젝트 수정
  @PatchMapping("/{projectId}")
  public ResponseEntity<ProjectDetailResponseDto> updateProject(
      @PathVariable Long projectId,
      @RequestBody ProjectUpdateRequestDto dto,
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    return ResponseEntity.ok(
        projectService.updateProject(projectId, dto, user)
    );
  }

  //프로젝트 삭제
  @DeleteMapping("/{projectId}")
  public ResponseEntity<?> deleteProject(
      @PathVariable Long projectId,
      @AuthenticationPrincipal CustomUserDetails user) {

    projectService.deleteProject(projectId, user);
    return ResponseEntity.ok(Map.of("message", "프로젝트가 삭제 되었습니다."));
  }

  //프로젝트 조회(내부용)
  @GetMapping("/titles")
  public ResponseEntity<List<ProjectTitleResponseDto>> getProjectTitles(
      @RequestParam(required = false) String keyword
  ) {
    List<ProjectTitleResponseDto> result = projectService.getProjectTitles(keyword);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/titles/{projectId}")
  public ResponseEntity<ProjectMetaResponseDto> getProjectMeta(
      @PathVariable Long projectId
  ) {
    ProjectMetaResponseDto result = projectService.getProjectMeta(projectId);
    return ResponseEntity.ok(result);
  }
}

