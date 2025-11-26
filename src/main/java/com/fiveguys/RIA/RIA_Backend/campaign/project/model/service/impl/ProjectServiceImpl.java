package com.fiveguys.RIA.RIA_Backend.campaign.project.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.PermissionValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.component.PipelinePolicy;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.component.ProjectLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.component.ProjectMapper;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.component.ProjectValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectSearchRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectMetaResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectPipelineResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectTitleResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.repository.PipelineRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.service.ProjectService;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final PipelineRepository pipelineRepository;
  private final PermissionValidator permissionValidator;
  private final ProjectLoader projectLoader;
  private final ProjectMapper projectMapper;
  private final ProjectValidator projectValidator;
  private final PipelinePolicy pipelinePolicy;

  // 프로젝트 생성
  @Override
  @Transactional
  public ProjectCreateResponseDto createProject(ProjectCreateRequestDto dto, Long userId) {

    // 1. 입력 검증
    projectValidator.validateCreate(dto);

    // 2. 연관 로딩
    Long managerId = (dto.getSalesManagerId() != null) ? dto.getSalesManagerId() : userId;

    User manager = projectLoader.loadUser(managerId);
    ClientCompany company = projectLoader.loadClientCompany(dto.getClientCompanyId());
    Client client = projectLoader.loadClient(dto.getClientId());

    // 3. 중복 검증
    projectValidator.validateDuplicate(dto.getTitle(), company);

    // 4. 프로젝트 생성 (엔티티가 책임)
    Project project = Project.create(
        manager,
        company,
        client,
        dto.getTitle(),
        dto.toProjectType(),
        dto.getDescription(),
        dto.getStartDay(),
        dto.getEndDay(),
        dto.getExpectedRevenue(),
        dto.getExpectedMarginRate()
    );

    projectRepository.save(project);

    // 5. 파이프라인 생성
    Pipeline pipeline = pipelinePolicy.initializeOnProjectCreate(project);
    pipelineRepository.save(pipeline);

    // 6. 응답 DTO 변환
    return projectMapper.toCreateDto(project, pipeline);
  }

  // 프로젝트 목록 조회
  @Override
  @Transactional(readOnly = true)
  public List<ProjectPipelineResponseDto> getProjectsWithPipelines(
      Long userId,
      ProjectSearchRequestDto request,
      int page,
      int size
  ) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

    // 문자열 status → enum 변환 (validator 사용)
    Project.Status projectStatus = projectValidator.parseStatus(request.getStatus());

    List<Project> projects = projectRepository.findProjectsWithFilters(
        projectStatus,
        request.getKeyword(),
        request.getManagerName(),
        pageable
    );

    if (projects.isEmpty()) {
      return List.of();
    }

    return projects.stream()
        .map(projectMapper::toPipelineListDto)
        .collect(Collectors.toList());
  }

  // 프로젝트 상세 조회
  @Override
  @Transactional
  public ProjectDetailResponseDto getProjectDetail(Long projectId) {

    Project project = projectLoader.loadDetail(projectId);

    return projectMapper.toDetailDto(project);
  }

  //프로젝트 수정
  @Override
  @Transactional
  public ProjectDetailResponseDto updateProject(
      Long projectId,
      ProjectUpdateRequestDto dto,
      CustomUserDetails user
  ) {

    // 1. 조회
    Project project = projectLoader.loadProjectWithSalesManager(projectId);

    // 2. 권한검증
    permissionValidator.validateOwnerOrLeadOrAdmin(project.getSalesManager(), user);

    // 3. 입력값 검증
    projectValidator.validateUpdate(dto);

    // 4. 엔티티 patch 책임
    project.update(
        dto.getTitle(),
        dto.getDescription(),
        dto.toProjectType(),
        dto.getExpectedRevenue(),
        dto.getExpectedMarginRate(),
        dto.getStartDay(),
        dto.getEndDay()
    );

    // 5. 응답 생성
    return projectMapper.toDetailDto(project);
  }

  //삭제
  @Override
  @Transactional
  public void deleteProject(Long projectId, CustomUserDetails user) {

    // 1. 조회
    Project project = projectLoader.loadProjectWithSalesManager(projectId);

    // 2. 권한 검증
    permissionValidator.validateOwnerOrLeadOrAdmin(project.getSalesManager(), user);

    // 3. 상태 검증
    projectValidator.validateCancelable(project);

    // 4. 도메인 취소 처리
    project.cancel();

    log.info("프로젝트 [{}]가 사용자 [{}]에 의해 CANCELED로 변경됨",
        project.getProjectId(), user.getUsername());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProjectTitleResponseDto> getProjectTitles(String keyword) {
    String k = (keyword == null || keyword.isBlank()) ? null : keyword;

    List<Project> projects = projectRepository.findTitleOptions(k);

    return projectMapper.toTitleDtoList(projects);
  }

  @Override
  @Transactional(readOnly = true)
  public ProjectMetaResponseDto getProjectMeta(Long projectId) {
    Project project = projectLoader.loadProject(projectId);

    Long companyId = project.getClientCompany() != null
        ? project.getClientCompany().getId()
        : null;

    Long clientId = project.getClient() != null
        ? project.getClient().getId()
        : null;

    ClientCompany company = projectLoader.loadClientCompany(companyId);
    Client client = projectLoader.loadClient(clientId);

    return projectMapper.toProjectMetaDto(project, company, client);
  }


  @Override
  @Transactional
  public void updateProjectManager(Long projectId, Long newManagerId, Long actorId) {

    // 1. 프로젝트 로딩
    Project project = projectLoader.loadProject(projectId);
    // 2-1. 요청 보낸 사람 로딩
    User actor = projectLoader.loadUser(actorId);
    projectValidator.validateManagerChangePermission(actor);

    // 2-2. 권한 검증 (ADMIN / SALES_LEAD 만 허용)
    projectValidator.validateManagerChange(project, newManagerId);

    // 3. 새 담당자 로딩
    User newManager = projectLoader.loadUser(newManagerId);

    // 4. 실제 변경
    project.updateSalesManager(newManager);
    // 영속 상태이므로 save 호출 불필요
  }
}



