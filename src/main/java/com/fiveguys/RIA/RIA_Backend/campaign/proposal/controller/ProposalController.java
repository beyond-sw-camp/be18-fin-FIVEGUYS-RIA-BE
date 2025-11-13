package com.fiveguys.RIA.RIA_Backend.campaign.proposal.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.impl.ProposalServiceImpl;
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

  @PostMapping
  public ResponseEntity<ProposalCreateResponseDto> createProposal(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody ProposalCreateRequestDto dto
  ) {
    ProposalCreateResponseDto response = proposalService.createProposal(dto, user.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
