package com.fiveguys.RIA.RIA_Backend.campaign.project.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectManagerUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectSearchRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectMetaResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectPipelinePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectTitleResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

  @Mock
  private ProjectService projectService;

  @InjectMocks
  private ProjectController projectController;

  @Test
  @DisplayName("createProject: 프로젝트 생성 성공 시 201 + 응답 DTO 반환")
  void createProject_success() {
    Long userId = 1L;

    ProjectCreateRequestDto requestDto = mock(ProjectCreateRequestDto.class);
    ProjectCreateResponseDto responseDto = mock(ProjectCreateResponseDto.class);

    given(projectService.createProject(requestDto, userId)).willReturn(responseDto);

    ResponseEntity<ProjectCreateResponseDto> result =
        projectController.createProject(requestDto, userId);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(result.getBody()).isSameAs(responseDto);
    verify(projectService).createProject(requestDto, userId);
  }

  @Test
  @DisplayName("getProjectsWithPipelines: 파이프라인 포함 프로젝트 목록 조회")
  void getProjectsWithPipelines_success() {
    Long userId = 1L;
    int page = 2;
    int size = 12;

    ProjectSearchRequestDto searchDto = mock(ProjectSearchRequestDto.class);
    ProjectPipelinePageResponseDto pageDto = mock(ProjectPipelinePageResponseDto.class);

    given(projectService.getProjectsWithPipelines(userId, searchDto, page, size))
        .willReturn(pageDto);

    ResponseEntity<ProjectPipelinePageResponseDto> result =
        projectController.getProjectsWithPipelines(userId, searchDto, page, size);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(pageDto);
    verify(projectService).getProjectsWithPipelines(userId, searchDto, page, size);
  }

  @Test
  @DisplayName("getProjectDetail: 프로젝트 상세 조회")
  void getProjectDetail_success() {
    Long projectId = 10L;
    ProjectDetailResponseDto detailDto = mock(ProjectDetailResponseDto.class);

    given(projectService.getProjectDetail(projectId)).willReturn(detailDto);

    ProjectDetailResponseDto result = projectController.getProjectDetail(projectId);

    assertThat(result).isSameAs(detailDto);
    verify(projectService).getProjectDetail(projectId);
  }

  @Test
  @DisplayName("updateProject: 프로젝트 수정")
  void updateProject_success() {
    Long projectId = 10L;

    ProjectUpdateRequestDto updateDto = mock(ProjectUpdateRequestDto.class);
    CustomUserDetails userDetails = mock(CustomUserDetails.class);
    ProjectDetailResponseDto updatedDto = mock(ProjectDetailResponseDto.class);

    given(projectService.updateProject(projectId, updateDto, userDetails))
        .willReturn(updatedDto);

    ResponseEntity<ProjectDetailResponseDto> result =
        projectController.updateProject(projectId, updateDto, userDetails);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(updatedDto);
    verify(projectService).updateProject(projectId, updateDto, userDetails);
  }

  @Test
  @DisplayName("deleteProject: 프로젝트 삭제 시 서비스 호출 + 메시지 반환")
  void deleteProject_success() {
    Long projectId = 10L;
    CustomUserDetails userDetails = mock(CustomUserDetails.class);

    ResponseEntity<?> result = projectController.deleteProject(projectId, userDetails);

    @SuppressWarnings("unchecked")
    Map<String, String> body = (Map<String, String>) result.getBody();

    assertThat(body)
        .containsEntry("message", "프로젝트가 삭제 되었습니다.");

    verify(projectService).deleteProject(projectId, userDetails);
  }

  @Test
  @DisplayName("getProjectTitles: 프로젝트 타이틀 목록 조회")
  void getProjectTitles_success() {
    String keyword = "ABC";

    ProjectTitleResponseDto t1 = mock(ProjectTitleResponseDto.class);
    ProjectTitleResponseDto t2 = mock(ProjectTitleResponseDto.class);
    List<ProjectTitleResponseDto> list = List.of(t1, t2);

    given(projectService.getProjectTitles(keyword)).willReturn(list);

    ResponseEntity<List<ProjectTitleResponseDto>> result =
        projectController.getProjectTitles(keyword);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).containsExactly(t1, t2);
    verify(projectService).getProjectTitles(keyword);
  }

  @Test
  @DisplayName("getProjectMeta: 단일 프로젝트 메타 정보 조회")
  void getProjectMeta_success() {
    Long projectId = 10L;
    ProjectMetaResponseDto metaDto = mock(ProjectMetaResponseDto.class);

    given(projectService.getProjectMeta(projectId)).willReturn(metaDto);

    ResponseEntity<ProjectMetaResponseDto> result =
        projectController.getProjectMeta(projectId);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(metaDto);
    verify(projectService).getProjectMeta(projectId);
  }

  @Test
  @DisplayName("changeSalesManager: 담당자 변경")
  void changeSalesManager_success() {
    Long projectId = 10L;
    Long actorId = 99L;
    Long newManagerId = 3L;

    ProjectManagerUpdateRequestDto dto = mock(ProjectManagerUpdateRequestDto.class);
    when(dto.getNewManagerId()).thenReturn(newManagerId);

    CustomUserDetails userDetails = mock(CustomUserDetails.class);
    when(userDetails.getUserId()).thenReturn(actorId);

    ResponseEntity<Void> result =
        projectController.changeSalesManager(projectId, dto, userDetails);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(result.getBody()).isNull();
    verify(projectService).updateProjectManager(projectId, newManagerId, actorId);
  }
}
