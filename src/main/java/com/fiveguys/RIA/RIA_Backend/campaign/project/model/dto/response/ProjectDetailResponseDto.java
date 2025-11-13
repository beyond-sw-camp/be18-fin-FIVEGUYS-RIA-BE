package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineInfoResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalSummaryDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDetailResponseDto {
  private Long projectId;
  private String title;
  private String description;
  private String type;
  private int expectedRevenue;
  private BigDecimal expectedMarginRate;
  private String clientCompanyName;
  private String clientName;
  private String salesManagerName;
  private LocalDate startDay;
  private LocalDate endDay;
  private String status;
  private PipelineInfoResponseDto pipelineInfo;
  private List<PipelineStageResponseDto> stageList;
  private List<ProposalSummaryDto> proposals;
  // private List<EstimateSummaryDto> estimates; // TODO: 견적
  // private List<ContractSummaryDto> contracts; // TODO: 계약
  // private List<RevenueSummaryDto> revenues;   // TODO: 매출
}