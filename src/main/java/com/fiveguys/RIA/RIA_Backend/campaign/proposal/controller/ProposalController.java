package com.fiveguys.RIA.RIA_Backend.campaign.proposal.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalSimpleDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.ProposalService;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalPageResponseDto;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proposals")
@Tag(name = "Proposal", description = "제안서 관리 API")
public class ProposalController {

  private final ProposalService proposalService;
  //생성 하기
  @PostMapping
  @Operation(
      summary = "제안서 생성",
      description = "프로젝트/고객사 정보 기반 제안서를 생성한다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "생성 성공",
          content = @Content(schema = @Schema(implementation = ProposalCreateResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<ProposalCreateResponseDto> createProposal(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody ProposalCreateRequestDto dto
  ) {
    ProposalCreateResponseDto response = proposalService.createProposal(dto, user.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 목록 조회하기
  @GetMapping
  @Operation(
      summary = "제안서 목록 조회",
      description = "프로젝트/고객사/키워드/상태 기반으로 제안서 목록을 조회한다."
  )
  @Parameters({
      @Parameter(name = "projectId", description = "프로젝트 ID"),
      @Parameter(name = "clientCompanyId", description = "고객사 ID"),
      @Parameter(name = "keyword", description = "검색 키워드"),
      @Parameter(name = "status", description = "제안 상태", schema = @Schema(implementation = Proposal.Status.class)),
      @Parameter(name = "page", description = "페이지 번호", example = "1"),
      @Parameter(name = "size", description = "페이지 크기", example = "10")
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ProposalPageResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })

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
  @Operation(
      summary = "제안서 상세 조회",
      description = "제안서 ID로 상세 정보를 조회한다."
  )
  @Parameter(name = "proposalId", description = "제안서 ID", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ProposalDetailResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "대상 제안 없음"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<ProposalDetailResponseDto> getProposalDetail(
      @PathVariable Long proposalId
  ) {
    ProposalDetailResponseDto dto = proposalService.getProposalDetail(proposalId);
    return ResponseEntity.ok(dto);
  }

  //수정하기
  @PatchMapping("/{proposalId}")
  @Operation(
      summary = "제안서 수정",
      description = "제안서에 대한 상세 정보를 수정한다."
  )
  @Parameter(name = "proposalId", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(schema = @Schema(implementation = ProposalDetailResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "대상 제안 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
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
  @Operation(
      summary = "제안서 삭제",
      description = "제안서를 삭제한다."
  )
  @Parameter(name = "proposalId", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "삭제 성공"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "대상 제안 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<?> deleteProposal(
      @PathVariable Long proposalId,
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    proposalService.deleteProposal(proposalId, user);
    return ResponseEntity.ok(Map.of("message", "제안이 삭제되었습니다."));
  }
  //견적용 제안 목록 조회
  @GetMapping("/projects/{projectId}")
  public ResponseEntity<List<ProposalSimpleDto>> getProposalsByProject(
          @PathVariable Long projectId
  ) {
    return ResponseEntity.ok(proposalService.getSimpleProposals(projectId));
  }
}

