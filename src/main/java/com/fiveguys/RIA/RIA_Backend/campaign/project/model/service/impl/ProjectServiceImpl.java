package com.fiveguys.RIA.RIA_Backend.campaign.project.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.PermissionValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline.Status;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectPipelineResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.repository.PipelineRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.service.ProjectService;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalSummaryDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final PipelineRepository pipelineRepository;
  private final ClientCompanyRepository clientCompanyRepository;
  private final ClientRepository clientRepository;
  private final UserRepository userRepository;
  private final PermissionValidator permissionValidator;

  // 프로젝트 생성
  @Override
  public ProjectCreateResponseDto createProject(ProjectCreateRequestDto dto, Long userId) {

    Long managerId = dto.getSalesManagerId() != null ? dto.getSalesManagerId() : userId;

    User salesManager = userRepository.findById(managerId)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.SALES_MANAGER_NOT_FOUND));

    ClientCompany clientCompany = clientCompanyRepository.findById(dto.getClientCompanyId())
        .orElseThrow(() -> new CustomException(ProjectErrorCode.CLIENT_COMPANY_NOT_FOUND));

    Client client = clientRepository.findById(dto.getClientId())
        .orElseThrow(() -> new CustomException(ProjectErrorCode.CLIENT_NOT_FOUND));

    if (projectRepository.existsByTitleAndClientCompany(dto.getTitle(), clientCompany)) {
      throw new CustomException(ProjectErrorCode.DUPLICATE_PROJECT);
    }

    Project project = Project.builder()
        .salesManager(salesManager)
        .clientCompany(clientCompany)
        .client(client)
        .title(dto.getTitle())
        .type(Project.Type.valueOf(dto.getType()))
        .description(dto.getDescription())
        .status(Project.Status.ACTIVE)
        .startDay(dto.getStartDay())
        .endDay(dto.getEndDay())
        .expectedRevenue(dto.getExpectedRevenue())
        .expectedMarginRate(dto.getExpectedMarginRate())
        .build();

    projectRepository.save(project);

    // 파이프라인 자동 생성
    Pipeline pipeline = Pipeline.builder()
        .project(project)
        .currentStage(1)
        .stageName(Pipeline.StageName.PROPOSAL_RECEIVED)
        .status(Status.ACTIVE)
        .build();

    pipelineRepository.save(pipeline);

    return ProjectCreateResponseDto.builder()
        .projectId(project.getProjectId())
        .title(project.getTitle())
        .status(project.getStatus().name())
        .createdAt(project.getCreatedAt())
        .pipelineId(pipeline.getPipelineId())
        .build();
  }

  // 프로젝트 목록 조회
  @Override
  public List<ProjectPipelineResponseDto> getProjectsWithPipelines(
      Long userId, String status, String keyword, String managerName, int page, int size
  ) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

    Project.Status projectStatus = null;
    if (status != null && !status.isBlank()) {
      try {
        projectStatus = Project.Status.valueOf(status.toUpperCase());
      } catch (IllegalArgumentException e) {
        log.warn("잘못된 상태값: {}", status);
      }
    }

    List<Project> projects = projectRepository.findProjectsWithFilters(
        projectStatus, keyword, managerName, pageable
    );

    if (projects.isEmpty()) return List.of();

    return projects.stream().map(project -> {
      Pipeline pipeline = project.getPipeline();

      //  파이프라인 정보 생성 (단계 기준으로만 진행률 계산)
      PipelineInfoResponseDto pipelineInfo = pipeline != null
          ? PipelineInfoResponseDto.from(pipeline)
          : null;

      //  단계별 리스트 구성
      List<PipelineStageResponseDto> stages = new ArrayList<>();
      if (pipeline != null) {
        for (Pipeline.StageName stageName : Pipeline.StageName.values()) {
          int stageNo = stageName.ordinal() + 1;
          boolean isCompleted = stageNo < pipeline.getCurrentStage();
          LocalDateTime completedAt =
              isCompleted && stageNo == pipeline.getCurrentStage() - 1
                  ? pipeline.getCreatedAt()
                  : null;

          stages.add(PipelineStageResponseDto.builder()
              .stageNo(stageNo)
              .stageName(stageName.getDisplayName())
              .isCompleted(isCompleted)
              .completedAt(completedAt)
              .build());
        }
      }

      return ProjectPipelineResponseDto.builder()
          .projectId(project.getProjectId())
          .title(project.getTitle())
          .clientCompanyName(project.getClientCompany().getCompanyName())
          .clientName(project.getClient().getName())
          .planningDate(project.getCreatedAt().toLocalDate())
          .startDay(project.getStartDay())
          .endDay(project.getEndDay())
          .salesManagerName(project.getSalesManager().getName())
          .status(project.getStatus().name())
          .pipelineInfo(pipelineInfo)
          .stageList(stages)
          .build();
    }).collect(Collectors.toList());
  }

  // 프로젝트 상세 조회
  @Override
  public ProjectDetailResponseDto getProjectDetail(Long projectId) {
    Project project = projectRepository.findByProjectId(projectId)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.PROJECT_NOT_FOUND));

    Pipeline pipeline = project.getPipeline();

    PipelineInfoResponseDto pipelineInfo = pipeline != null
        ? PipelineInfoResponseDto.from(pipeline)
        : null;

    List<PipelineStageResponseDto> stages = new ArrayList<>();
    if (pipeline != null) {
      for (Pipeline.StageName stageName : Pipeline.StageName.values()) {
        int stageNo = stageName.ordinal() + 1;

        // 현재 단계 이전까지만 완료 처리
        boolean isCompleted = stageNo < pipeline.getCurrentStage();

        // 현재 진행 중인 단계의 바로 이전 단계 완료 시점 저장
        LocalDateTime completedAt =
            isCompleted && stageNo == pipeline.getCurrentStage() - 1
                ? pipeline.getCreatedAt()
                : null;

        stages.add(PipelineStageResponseDto.builder()
            .stageNo(stageNo)
            .stageName(stageName.getDisplayName())
            .isCompleted(isCompleted)
            .completedAt(completedAt)
            .build());
      }
    }

    // 프로젝트에 연결된 제안 요약 리스트
    List<ProposalSummaryDto> proposals = project.getProposals().stream()
        .map(p -> ProposalSummaryDto.builder()
            .proposalId(p.getProposalId())
            .title(p.getTitle())
            .writerName(p.getCreatedUser().getName())
            .requestDate(p.getRequestDate())
            .submitDate(p.getSubmitDate())
            .description(p.getData())
            .build())
        .collect(Collectors.toList());

    // 상세 응답 DTO 반환
    return ProjectDetailResponseDto.builder()
        .projectId(project.getProjectId())
        .title(project.getTitle())
        .description(project.getDescription())
        .type(project.getType().name())
        .expectedRevenue(project.getExpectedRevenue())
        .expectedMarginRate(project.getExpectedMarginRate())
        .clientCompanyName(project.getClientCompany().getCompanyName())
        .clientName(project.getClient().getName())
        .salesManagerName(project.getSalesManager().getName())
        .startDay(project.getStartDay())
        .endDay(project.getEndDay())
        .status(project.getStatus().name())
        .pipelineInfo(pipelineInfo)
        .stageList(stages)
        .proposals(proposals)
        .build();
  }

  //프로젝트 수정
  @Override
  @Transactional
  public ProjectDetailResponseDto updateProject(Long projectId, ProjectUpdateRequestDto dto, CustomUserDetails user) {

    //  프로젝트 조회 (SalesManager 포함)
    Project project = projectRepository.findByIdWithSalesManager(projectId)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.PROJECT_NOT_FOUND));

    //  권한 검증
    permissionValidator.validateOwnerOrLeadOrAdmin(project.getSalesManager(), user);

    //  PATCH 적용 (엔티티가 책임)
    project.applyPatch(
        dto.getTitle(),
        dto.getDescription(),
        dto.toProjectType(),
        dto.getExpectedRevenue(),
        dto.getExpectedMarginRate(),
        dto.getStartDay(),
        dto.getEndDay()
    );

    //  JPA 더티체킹으로 자동 저장 (save 불필요)
    Pipeline pipeline = project.getPipeline();
    PipelineInfoResponseDto pipelineInfo =
        (pipeline != null) ? PipelineInfoResponseDto.from(pipeline) : null;

    //  DTO 반환
    return ProjectDetailResponseDto.builder()
        .projectId(project.getProjectId())
        .title(project.getTitle())
        .description(project.getDescription())
        .type(project.getType() != null ? project.getType().name() : null)
        .expectedRevenue(project.getExpectedRevenue())
        .expectedMarginRate(project.getExpectedMarginRate())
        .clientCompanyName(project.getClientCompany().getCompanyName())
        .clientName(project.getClient().getName())
        .salesManagerName(project.getSalesManager().getName())
        .startDay(project.getStartDay())
        .endDay(project.getEndDay())
        .status(project.getStatus().name())
        .pipelineInfo(pipelineInfo)
        .build();
  }

  //삭제
  @Override
  @Transactional
  public void deleteProject(Long projectId, CustomUserDetails user) {
    Project project = projectRepository.findByIdWithSalesManager(projectId)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.PROJECT_NOT_FOUND));

    permissionValidator.validateOwnerOrLeadOrAdmin(project.getSalesManager(), user);

    if (project.getStatus() == Project.Status.COMPLETED) {
      throw new CustomException(ProjectErrorCode.CANNOT_CANCEL_COMPLETED_PROJECT);
    }

    project.cancel();

    log.info("프로젝트 [{}]가 사용자 [{}]에 의해 CANCELED로 변경됨",
        project.getProjectId(), user.getUsername());
  }
}
