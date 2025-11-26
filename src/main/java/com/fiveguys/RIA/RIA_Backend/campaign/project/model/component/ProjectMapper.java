package com.fiveguys.RIA.RIA_Backend.campaign.project.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectMetaResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectPipelineResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectTitleResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalSummaryDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

  public ProjectCreateResponseDto toCreateDto(Project p, Pipeline pipeline) {
    return ProjectCreateResponseDto.builder()
        .projectId(p.getProjectId())
        .title(p.getTitle())
        .status(p.getStatus().name())
        .createdAt(p.getCreatedAt())
        .pipelineId(pipeline != null ? pipeline.getPipelineId() : null)
        .build();
  }

  public ProjectDetailResponseDto toDetailDto(Project p) {

    Pipeline pipeline = p.getPipeline();

    PipelineInfoResponseDto pipelineInfo =
        (pipeline != null) ? PipelineInfoResponseDto.from(pipeline) : null;

    List<PipelineStageResponseDto> stages = buildPipelineStages(pipeline);

    List<ProposalSummaryDto> proposals = p.getProposals().stream()
        .map(prop -> ProposalSummaryDto.builder()
            .proposalId(prop.getProposalId())
            .title(prop.getTitle())
            .writerName(prop.getCreatedUser().getName())
            .requestDate(prop.getRequestDate())
            .submitDate(prop.getSubmitDate())
            .description(prop.getData())
            .build())
        .collect(Collectors.toList());

    return ProjectDetailResponseDto.builder()
        .projectId(p.getProjectId())
        .title(p.getTitle())
        .description(p.getDescription())
        .type(p.getType().name())
        .expectedRevenue(p.getExpectedRevenue())
        .expectedMarginRate(p.getExpectedMarginRate())
        .clientCompanyName(p.getClientCompany().getCompanyName())
        .clientName(p.getClient().getName())
        .salesManagerName(p.getSalesManager().getName())
        .startDay(p.getStartDay())
        .endDay(p.getEndDay())
        .status(p.getStatus().name())
        .pipelineInfo(pipelineInfo)
        .stageList(stages)
        .proposals(proposals)
        .build();
  }

  public ProjectPipelineResponseDto toPipelineListDto(Project p) {
    Pipeline pipeline = p.getPipeline();

    PipelineInfoResponseDto pipelineInfo =
        (pipeline != null) ? PipelineInfoResponseDto.from(pipeline) : null;

    List<PipelineStageResponseDto> stages = buildPipelineStages(pipeline);

    return ProjectPipelineResponseDto.builder()
        .projectId(p.getProjectId())
        .title(p.getTitle())
        .clientCompanyName(p.getClientCompany().getCompanyName())
        .clientName(p.getClient().getName())
        .planningDate(p.getCreatedAt().toLocalDate())
        .startDay(p.getStartDay())
        .endDay(p.getEndDay())
        .salesManagerName(p.getSalesManager().getName())
        .status(p.getStatus().name())
        .pipelineInfo(pipelineInfo)
        .stageList(stages)
        .build();
  }

  private List<PipelineStageResponseDto> buildPipelineStages(Pipeline pipeline) {

    List<PipelineStageResponseDto> stages = new ArrayList<>();

    if (pipeline == null) return stages;

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

    return stages;
  }

  public ProjectTitleResponseDto toTitleDto(Project project) {
    return ProjectTitleResponseDto.builder()
        .projectId(project.getProjectId())
        .projectTitle(project.getTitle())
        .build();
  }

  public List<ProjectTitleResponseDto> toTitleDtoList(List<Project> projects) {
    return projects.stream()
        .map(this::toTitleDto)
        .collect(Collectors.toList());
  }

  public ProjectMetaResponseDto toProjectMetaDto(
      Project project,
      ClientCompany company,
      Client client
  ) {
    return ProjectMetaResponseDto.builder()
        .projectId(project.getProjectId())
        .projectName(project.getTitle())
        .clientCompanyId(company.getId())
        .clientCompanyName(company.getCompanyName())
        .clientId(client.getId())
        .clientName(client.getName())
        .build();
  }
}