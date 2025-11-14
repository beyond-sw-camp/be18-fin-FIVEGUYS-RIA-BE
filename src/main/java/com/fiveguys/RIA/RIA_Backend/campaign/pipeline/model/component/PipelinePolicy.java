package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PipelinePolicy {

  // 프로젝트 생성시 파이프라인 자동 생성
  public Pipeline initializeOnProjectCreate(Project project) {
    if (project == null) return null;

    return Pipeline.builder()
        .project(project)
        .currentStage(1)
        .stageName(Pipeline.StageName.PROPOSAL_RECEIVED)
        .status(Pipeline.Status.ACTIVE)
        .build();
  }

 //제안서 연결시 파이프라인 자동 업데이트
  public void handleProposalCreated(Pipeline pipeline, Project project) {
    if (pipeline == null || project == null) return;

    // 기존 로직 유지
    if (pipeline.getCurrentStage() == 1) {
      pipeline.updateStage(
          2,
          Pipeline.StageName.INTERNAL_REVIEW,
          Pipeline.Status.ACTIVE
      );
    }
  }
}