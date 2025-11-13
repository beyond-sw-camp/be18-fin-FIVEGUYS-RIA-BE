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
  private Long projectId;
  private String title;
  private String clientCompanyName;
  private String clientName;
  private LocalDate planningDate;
  private LocalDate startDay;
  private LocalDate endDay;
  private String salesManagerName;
  private String status;

  private PipelineInfoResponseDto pipelineInfo;
  private List<PipelineStageResponseDto> stageList;
}