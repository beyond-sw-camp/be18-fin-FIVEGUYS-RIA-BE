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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@Tag(name = "Project", description = "프로젝트 관리 API")

public class ProjectController {

  private final ProjectService projectService;

  //프로젝트 생성
  @PostMapping
  @Operation(
      summary = "프로젝트 생성",
      description = "영업 캠페인 프로젝트를 신규 생성한다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공",
          content = @Content(schema = @Schema(implementation = ProjectCreateResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<ProjectCreateResponseDto> createProject(
      @RequestBody ProjectCreateRequestDto dto,
      @AuthenticationPrincipal(expression = "userId") Long userId) {

    ProjectCreateResponseDto response = projectService.createProject(dto, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  //프로젝트 목록 조회
  @GetMapping("/pipelines")
  @Operation(
      summary = "프로젝트 목록(파이프라인 포함) 조회",
      description = "검색 조건과 페이징 정보를 기준으로 프로젝트 목록과 파이프라인 정보를 함께 조회한다."
  )
  @Parameters({
      @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1"),
      @Parameter(name = "size", description = "페이지 크기", example = "12")
      // ProjectSearchRequestDto 필드는 쿼리 파라미터로 자동 매핑됨
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ProjectPipelinePageResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<ProjectPipelinePageResponseDto> getProjectsWithPipelines(
      @AuthenticationPrincipal(expression = "userId") Long userId,
      @ModelAttribute ProjectSearchRequestDto request,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "12") int size
  ) {
    ProjectPipelinePageResponseDto result =
        projectService.getProjectsWithPipelines(userId, request, page, size);
    return ResponseEntity.ok(result);
  }


  //프로젝트 상세조회
  @GetMapping("/{projectId}")
  @Operation(
      summary = "프로젝트 상세 조회",
      description = "프로젝트 ID로 상세 정보를 조회한다."
  )
  @Parameter(name = "projectId", description = "프로젝트 ID", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ProjectDetailResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "해당 프로젝트 없음"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ProjectDetailResponseDto getProjectDetail(@PathVariable Long projectId) {
    return projectService.getProjectDetail(projectId);
  }

  //프로젝트 수정
  @PatchMapping("/{projectId}")
  @Operation(
      summary = "프로젝트 수정",
      description = "프로젝트 기본 정보를 수정한다."
  )
  @Parameter(name = "projectId", description = "프로젝트 ID", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(schema = @Schema(implementation = ProjectDetailResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 프로젝트 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
                            })
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
  @Operation(
      summary = "프로젝트 삭제",
      description = "프로젝트를 논리/물리 삭제 처리한다."
  )
  @Parameter(name = "projectId", description = "프로젝트 ID", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "삭제 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 프로젝트 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<?> deleteProject(
      @PathVariable Long projectId,
      @AuthenticationPrincipal CustomUserDetails user) {

    projectService.deleteProject(projectId, user);
    return ResponseEntity.ok(Map.of("message", "프로젝트가 삭제 되었습니다."));
  }

  //프로젝트 조회(내부용)
  @GetMapping("/titles")
  @Operation(
      summary = "프로젝트 제목 목록 조회(내부용)",
      description = "검색 키워드 기반으로 프로젝트 제목 리스트를 조회한다."
  )
  @Parameter(name = "keyword", description = "프로젝트명 검색 키워드", required = false)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ProjectTitleResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<List<ProjectTitleResponseDto>> getProjectTitles(
      @RequestParam(required = false) String keyword
  ) {
    List<ProjectTitleResponseDto> result = projectService.getProjectTitles(keyword);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/titles/{projectId}")
  @Operation(
      summary = "프로젝트 메타 정보 조회(내부용)",
      description = "프로젝트 제목 선택 시 필요한 메타 정보를 조회한다."
  )
  @Parameter(name = "projectId", description = "프로젝트 ID", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ProjectMetaResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "해당 프로젝트 없음"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<ProjectMetaResponseDto> getProjectMeta(
      @PathVariable Long projectId
  ) {
    ProjectMetaResponseDto result = projectService.getProjectMeta(projectId);
    return ResponseEntity.ok(result);
  }

  //프로젝트 담당자 변경
  @PatchMapping("/{projectId}/salesManager")
  @Operation(
      summary = "프로젝트 담당자 변경",
      description = "프로젝트의 영업 담당자를 다른 사용자로 변경한다."
  )
  @Parameter(name = "projectId", description = "프로젝트 ID", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "변경 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "403", description = "변경 권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 프로젝트 또는 사용자 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<Void> changeSalesManager(
      @PathVariable Long projectId,
      @RequestBody ProjectManagerUpdateRequestDto dto,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {

    Long actorId = userDetails.getUserId();
    projectService.updateProjectManager(projectId, dto.getNewManagerId(), actorId);

    return ResponseEntity.noContent().build();
  }
}

