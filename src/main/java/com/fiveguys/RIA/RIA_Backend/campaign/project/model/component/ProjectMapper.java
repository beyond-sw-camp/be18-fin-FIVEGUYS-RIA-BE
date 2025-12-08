package com.fiveguys.RIA.RIA_Backend.campaign.project.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateSummaryDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.StoreEstimateMap;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline.StageName;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectMetaResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectPipelinePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectPipelineResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response.ProjectTitleResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalSummaryDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    // 제안 여부와 상관없이, 파이프라인만 있으면 pipelineInfo 생성
    PipelineInfoResponseDto pipelineInfo =
        (pipeline != null)
            ? PipelineInfoResponseDto.from(pipeline)
            : null;

    // buildPipelineStages 에서도 더 이상 hasProposal 로 막지 말고,
    // 현재 pipeline 상태만 보고 completed 계산하게 바꿔라.
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

    List<EstimateSummaryDto> estimates = p.getEstimates().stream()
        .map(e -> EstimateSummaryDto.builder()
            .estimateId(e.getEstimateId())
            .title(e.getEstimateTitle())
            .writerName(e.getCreatedUser().getName())
            .totalAmount(
                e.getStoreEstimateMaps().stream()
                    .mapToLong(StoreEstimateMap::getFinalEstimateAmount)
                    .sum()
            )
            .createdDate(e.getCreatedAt().toLocalDate())
            .remark(e.getRemark())
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
        .estimates(estimates)
        .build();
  }

  public ProjectPipelineResponseDto toPipelineListDto(Project p) {
    Pipeline pipeline = p.getPipeline();
    boolean hasProposal = !p.getProposals().isEmpty();   //  제안 존재 여부

    PipelineInfoResponseDto pipelineInfo =
        (pipeline != null && hasProposal)
            ? PipelineInfoResponseDto.from(pipeline)
            : null;

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
    int cs = 0;

    if (pipeline != null && pipeline.getCurrentStage() != null) {
      cs = pipeline.getCurrentStage();
    }

    final int currentStageNo = cs;   // ★ final 로 만들기

    return Arrays.stream(StageName.values())
        .map(stage -> {
          int stageNo = stage.getStageNo();
          return PipelineStageResponseDto.builder()
              .stageNo(stageNo)
              .stageName(stage.getDisplayName())
              .isCompleted(currentStageNo >= stageNo)
              .build();
        })
        .collect(Collectors.toList());
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
        .clientCompanyId(company != null ? company.getId() : null)
        .clientCompanyName(company != null ? company.getCompanyName() : null)
        .clientId(client != null ? client.getId() : null)
        .clientName(client != null ? client.getName() : null)
        .build();
  }

  public ProjectPipelinePageResponseDto toPipelinePageDto(
      Page<Project> result,
      int page,   // 1-based
      int size
  ) {
    List<ProjectPipelineResponseDto> content = result.getContent().stream()
        .map(this::toPipelineListDto)
        .toList();

    return ProjectPipelinePageResponseDto.builder()
        .content(content)
        .page(page)
        .size(size)
        .totalElements(result.getTotalElements())
        .totalPages(result.getTotalPages())
        .first(result.isFirst())
        .last(result.isLast())
        .build();
  }
}