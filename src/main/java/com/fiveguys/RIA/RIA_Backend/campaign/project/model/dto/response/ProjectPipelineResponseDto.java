package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageResponseDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectPipelineResponseDto {
  private final Long projectId;
  private final String title;
  private final String clientCompanyName;
  private final String clientName;
  private final LocalDate planningDate;
  private final LocalDate startDay;
  private final LocalDate endDay;
  private final String salesManagerName;
  private final String status;

  private final PipelineInfoResponseDto pipelineInfo;
  private final List<PipelineStageResponseDto> stageList;
}