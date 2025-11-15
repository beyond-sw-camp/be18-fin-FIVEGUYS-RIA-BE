package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.common.model.dto.PageResponseDto;

public interface ProposalService {
  //생성
  ProposalCreateResponseDto createProposal(ProposalCreateRequestDto dto, Long userId);

  //목록조회
  PageResponseDto<ProposalListResponseDto> getProposalList(
      Long projectId,
      Long clientCompanyId,
      String keyword,
      Proposal.Status status,
      int page,
      int size
  );

  //상세조회
  ProposalDetailResponseDto getProposalDetail(Long proposalId);

  //수정
  ProposalDetailResponseDto updateProposal(Long proposalId, ProposalUpdateRequestDto dto, CustomUserDetails user);

  void deleteProposal(Long proposalId, CustomUserDetails user);

}