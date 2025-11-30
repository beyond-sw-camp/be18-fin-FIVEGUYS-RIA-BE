package com.fiveguys.RIA.RIA_Backend.common.util;

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
        .currentStage(0)
        .stageName(Pipeline.StageName.PROPOSAL_RECEIVED)
        .status(Pipeline.Status.ACTIVE)
        .build();
  }

 //제안서 연결시 파이프라인 자동 업데이트
  public void handleProposalCreated(Pipeline pipeline, Project project) {
    if (pipeline == null || project == null) return;

    Integer current = pipeline.getCurrentStage();
    int currentStage = (current != null) ? current : 0;

    // 기존 로직 유지
    if (currentStage < 1) {
      pipeline.autoAdvance(
          1,
          Pipeline.StageName.PROPOSAL_RECEIVED,
          Pipeline.Status.ACTIVE
      );

    }
  }
    public void handleEstimateCreated(Pipeline pipeline, Project project) {
        if (pipeline == null || project == null) return;

        Integer current = pipeline.getCurrentStage();
        int currentStage = (current != null) ? current : 0;

        // 기존 로직 유지
        if (currentStage < 3) {
            pipeline.autoAdvance(
                    3,
                    Pipeline.StageName.ESTIMATE,
                    Pipeline.Status.ACTIVE
            );
        }
    }
}