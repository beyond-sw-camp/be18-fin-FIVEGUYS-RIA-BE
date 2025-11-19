package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalPageResponseDto<T> {
  private int page;
  private int size;
  private long totalCount;
  private List<T> data;
}