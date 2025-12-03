package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalSimpleDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalPageResponseDto;

import java.util.List;

public interface ProposalService {
  //생성
  ProposalCreateResponseDto createProposal(ProposalCreateRequestDto dto, Long userId);

  //목록조회
  ProposalPageResponseDto<ProposalListResponseDto> getProposalList(
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

  //삭제
  void deleteProposal(Long proposalId, CustomUserDetails user);

  //견적용 제안목록조회
  List<ProposalSimpleDto> getSimpleProposals(Long projectId);

}