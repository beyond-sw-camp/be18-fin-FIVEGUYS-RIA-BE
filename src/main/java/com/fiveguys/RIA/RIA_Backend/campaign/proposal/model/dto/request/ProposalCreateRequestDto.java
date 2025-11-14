package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProposalCreateRequestDto {

  @NotNull
  private Long clientCompanyId;

  @NotNull
  private Long clientId;

  @NotBlank
  private String title;

  @NotNull
  private LocalDate submitDate;

  private Long projectId;

  private Long pipelineId;

  private String data;

  private LocalDate requestDate;

  private LocalDate periodStart;

  private LocalDate periodEnd;

  private String remark;
}

