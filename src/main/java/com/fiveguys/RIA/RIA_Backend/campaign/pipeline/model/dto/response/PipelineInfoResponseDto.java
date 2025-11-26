package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline.StageName;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PipelineInfoResponseDto {

  private Long pipelineId;
  private String currentStage;
  private int progressRate;

  public static PipelineInfoResponseDto from(Pipeline pipeline) {
    if (pipeline == null) {
      return PipelineInfoResponseDto.builder()
          .pipelineId(null)
          .currentStage("대기")
          .progressRate(0)
          .build();
    }

    Integer currentStageNo = pipeline.getCurrentStage(); // 1 ~ 5 또는 null
    int totalStages = StageName.values().length;         // enum 개수 = 5

    int rate = 0;
    if (currentStageNo != null && currentStageNo > 0) {
      // ★ 내가 누른 스테이지까지 포함해서 퍼센트 계산
      rate = (int) Math.round((currentStageNo / (double) totalStages) * 100);
    }

    // 파이프라인 상태가 COMPLETED 이면 무조건 100% 보정
    if (pipeline.getStatus() == Status.COMPLETED) {
      rate = 100;
    }

    return PipelineInfoResponseDto.builder()
        .pipelineId(pipeline.getPipelineId())
        .currentStage(pipeline.getStageName().getDisplayName())
        .progressRate(rate)
        .build();
  }
}