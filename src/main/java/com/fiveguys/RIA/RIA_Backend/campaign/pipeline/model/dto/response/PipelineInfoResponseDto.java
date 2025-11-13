package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PipelineInfoResponseDto {
  private String currentStage;
  private int progressRate;

  public static PipelineInfoResponseDto from(Pipeline pipeline) {
    if (pipeline == null)
      return PipelineInfoResponseDto.builder()
          .currentStage("대기")
          .progressRate(0)
          .build();

    int stage = pipeline.getCurrentStage();

    // 현재 진행 중인 단계 기준 이전 단계까지만 채운다.
    int rate = switch (stage) {
      case 1 -> 0;    // 제안수신 중
      case 2 -> 20;   // 내부검토 중 (제안 완료)
      case 3 -> 40;   // 견적 중
      case 4 -> 60;   // 협상 중
      case 5 -> 80;   // 계약 진행 중
      default -> 0;
    };

    // 계약 완료면 100%
    if (pipeline.getStatus() == Pipeline.Status.COMPLETED && stage == 5) {
      rate = 100;
    }

    return PipelineInfoResponseDto.builder()
        .currentStage(pipeline.getStageName().getDisplayName())
        .progressRate(rate)
        .build();
  }
}