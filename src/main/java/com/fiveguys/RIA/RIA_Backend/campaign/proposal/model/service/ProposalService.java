package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;

public interface ProposalService {
  ProposalCreateResponseDto createProposal(ProposalCreateRequestDto dto, Long userId);
}