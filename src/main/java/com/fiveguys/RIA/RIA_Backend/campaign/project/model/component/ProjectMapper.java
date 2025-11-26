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
    boolean hasProposal = !p.getProposals().isEmpty();   // 제안 존재 여부

    PipelineInfoResponseDto pipelineInfo =
        (pipeline != null && hasProposal)       // 제안 없으면 null → 진행률 0
            ? PipelineInfoResponseDto.from(pipeline)
            : null;

    List<PipelineStageResponseDto> stages = buildPipelineStages(pipeline, hasProposal);

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
    boolean hasProposal = !p.getProposals().isEmpty();   //  제안 존재 여부

    PipelineInfoResponseDto pipelineInfo =
        (pipeline != null && hasProposal)
            ? PipelineInfoResponseDto.from(pipeline)
            : null;

    List<PipelineStageResponseDto> stages = buildPipelineStages(pipeline, hasProposal);

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


  private List<PipelineStageResponseDto> buildPipelineStages(Pipeline pipeline, boolean hasProposal) {

    List<PipelineStageResponseDto> stages = new ArrayList<>();

    // 파이프라인 없으면 빈 리스트
    if (pipeline == null) return stages;

    //  제안이 없으면: 드래프트 상태 → 전부 미완료
    if (!hasProposal) {
      for (Pipeline.StageName stageName : Pipeline.StageName.values()) {
        int stageNo = stageName.getStageNo();

        stages.add(PipelineStageResponseDto.builder()
            .stageNo(stageNo)
            .stageName(stageName.getDisplayName())
            .isCompleted(false)       // 전부 false
            .completedAt(null)
            .build());
      }
      return stages;
    }

    //  수동으로 파이프라인 스테이지 바꿀때
    int currentStageNo = (pipeline.getCurrentStage() != null)
        ? pipeline.getCurrentStage()
        : 0;
    /*
         currentStageNo 의미

        currentStageNo = 0  → 아무 단계도 완료 안 됨
        currentStageNo = 1  → 1단계 "제안 수신"까지 완료
        currentStageNo = 2  → 2단계 "제안 수신"까지 완료 <- 내부 검토는 그냥 보여주기식으로 생각함 의견있으면 말좀
        currentStageNo = 3  → 3단계 "견적 생성"까지 완료
        currentStageNo = 4  → 4단계 "계약"까지 완료
        currentStageNo = 5  → 5단계 "매출 생성"까지 완료 (전체 파이프라인 완료)

        isCompleted = (stageNo <= currentStageNo)
        → currentStageNo 값까지의 모든 스테이지가 완료로 표시된다.

        ---- 이벤트 발생 시 설정 규칙 예시 ----
        [1] 견적(Estimate)이 프로젝트에 처음 붙을 때
            → pipeline.updateCurrentStage(3);
            → 1~3단계 완료

        [2] 계약(Contract)이 프로젝트에 처음 붙을 때
            → pipeline.updateCurrentStage(4);
            → 1~4단계 완료

        [3] 매출 생성(Sales)이 발생했을 때
            → pipeline.updateCurrentStage(5);
            → 1~5단계 전부 완료 (파이프라인 종료)
    */

    for (Pipeline.StageName stageName : Pipeline.StageName.values()) {
      int stageNo = stageName.getStageNo();

      boolean isCompleted = stageNo <= currentStageNo;

      stages.add(PipelineStageResponseDto.builder()
          .stageNo(stageNo)
          .stageName(stageName.getDisplayName())
          .isCompleted(isCompleted)
          .completedAt(null)
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
        .clientCompanyId(company != null ? company.getId() : null)
        .clientCompanyName(company != null ? company.getCompanyName() : null)
        .clientId(client != null ? client.getId() : null)
        .clientName(client != null ? client.getName() : null)
        .build();
  }
}