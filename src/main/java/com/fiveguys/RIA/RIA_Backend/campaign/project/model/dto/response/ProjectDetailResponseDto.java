package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateSummaryDto;
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

  private final Long projectId;
  private final String title;
  private final String description;
  private final String type;
  private final BigDecimal expectedRevenue;
  private final BigDecimal expectedMarginRate;
  private final String clientCompanyName;
  private final String clientName;
  private final String salesManagerName;
  private final LocalDate startDay;
  private final LocalDate endDay;
  private final String status;
  private final PipelineInfoResponseDto pipelineInfo;
  private final List<PipelineStageResponseDto> stageList;
  private final List<ProposalSummaryDto> proposals;
  private List<EstimateSummaryDto> estimates;
  // private List<ContractSummaryDto> contracts; // TODO: 계약
  // private List<RevenueSummaryDto> revenues;   // TODO: 매출
}