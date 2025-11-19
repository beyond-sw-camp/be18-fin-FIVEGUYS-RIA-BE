package com.fiveguys.RIA.RIA_Backend.campaign.proposal.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.impl.ProposalServiceImpl;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalPageResponseDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proposals")
public class ProposalController {

  private final ProposalServiceImpl proposalService;

  //생성 하기
  @PostMapping
  public ResponseEntity<ProposalCreateResponseDto> createProposal(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody ProposalCreateRequestDto dto
  ) {
    ProposalCreateResponseDto response = proposalService.createProposal(dto, user.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 목록 조회하기
  @GetMapping
  public ResponseEntity<ProposalPageResponseDto<ProposalListResponseDto>> getProposals(
      @RequestParam(required = false) Long projectId,
      @RequestParam(required = false) Long clientCompanyId,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Proposal.Status status,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    ProposalPageResponseDto<ProposalListResponseDto> result =
        proposalService.getProposalList(projectId, clientCompanyId, keyword, status, page, size);
    return ResponseEntity.ok(result);
  }

  //상세 조회하기
  @GetMapping("/{proposalId}")
  public ResponseEntity<ProposalDetailResponseDto> getProposalDetail(
      @PathVariable Long proposalId
  ) {
    ProposalDetailResponseDto dto = proposalService.getProposalDetail(proposalId);
    return ResponseEntity.ok(dto);
  }

  //수정하기
  @PatchMapping("/{proposalId}")
  public ResponseEntity<ProposalDetailResponseDto> updateProposal(
      @PathVariable Long proposalId,
      @RequestBody ProposalUpdateRequestDto dto,
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    return ResponseEntity.ok(
        proposalService.updateProposal(proposalId, dto, user)
    );
  }
  //삭제하기
  @DeleteMapping("/{proposalId}")
  public ResponseEntity<?> deleteProposal(
      @PathVariable Long proposalId,
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    proposalService.deleteProposal(proposalId, user);
    return ResponseEntity.ok(Map.of("message", "제안이 삭제되었습니다."));
  }
}

