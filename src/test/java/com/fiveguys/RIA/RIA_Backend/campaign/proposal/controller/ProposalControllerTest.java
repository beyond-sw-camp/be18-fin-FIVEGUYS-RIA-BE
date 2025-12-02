package com.fiveguys.RIA.RIA_Backend.campaign.proposal.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalCreateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.service.ProposalService;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProposalControllerTest {

  @Mock
  private ProposalService proposalService;

  @InjectMocks
  private ProposalController proposalController;

  @Test
  @DisplayName("createProposal: 제안 생성 성공 시 201 + 응답바디 반환")
  void createProposal_success() {
    // given
    CustomUserDetails userDetails = mock(CustomUserDetails.class);
    Long userId = 1L;
    given(userDetails.getUserId()).willReturn(userId);

    ProposalCreateRequestDto requestDto = mock(ProposalCreateRequestDto.class);
    ProposalCreateResponseDto responseDto = mock(ProposalCreateResponseDto.class);

    given(proposalService.createProposal(requestDto, userId))
        .willReturn(responseDto);

    // when
    ResponseEntity<ProposalCreateResponseDto> result =
        proposalController.createProposal(userDetails, requestDto);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(result.getBody()).isSameAs(responseDto);
    verify(proposalService).createProposal(requestDto, userId);
  }

  @Test
  @DisplayName("getProposals: 제안 목록 조회 성공 시 200 + 페이지 DTO 반환")
  void getProposals_success() {
    // given
    Long projectId = 10L;
    Long clientCompanyId = 20L;
    String keyword = "검색어";
    Proposal.Status status = null; // 필터 미사용 케이스
    int page = 2;
    int size = 5;

    ProposalListResponseDto item = mock(ProposalListResponseDto.class);
    ProposalPageResponseDto<ProposalListResponseDto> pageDto =
        mock(ProposalPageResponseDto.class);

    given(proposalService.getProposalList(
        projectId, clientCompanyId, keyword, status, page, size
    )).willReturn(pageDto);

    // when
    ResponseEntity<ProposalPageResponseDto<ProposalListResponseDto>> result =
        proposalController.getProposals(projectId, clientCompanyId, keyword, status, page, size);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(pageDto);

    verify(proposalService).getProposalList(
        projectId, clientCompanyId, keyword, status, page, size
    );
  }

  @Test
  @DisplayName("getProposalDetail: 제안 상세 조회 성공")
  void getProposalDetail_success() {
    // given
    Long proposalId = 100L;
    ProposalDetailResponseDto dto = mock(ProposalDetailResponseDto.class);

    given(proposalService.getProposalDetail(proposalId)).willReturn(dto);

    // when
    ResponseEntity<ProposalDetailResponseDto> result =
        proposalController.getProposalDetail(proposalId);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(dto);
    verify(proposalService).getProposalDetail(proposalId);
  }

  @Test
  @DisplayName("updateProposal: 제안 수정 성공")
  void updateProposal_success() {
    // given
    Long proposalId = 100L;
    ProposalUpdateRequestDto requestDto = mock(ProposalUpdateRequestDto.class);
    CustomUserDetails userDetails = mock(CustomUserDetails.class);

    ProposalDetailResponseDto responseDto = mock(ProposalDetailResponseDto.class);
    given(proposalService.updateProposal(proposalId, requestDto, userDetails))
        .willReturn(responseDto);

    // when
    ResponseEntity<ProposalDetailResponseDto> result =
        proposalController.updateProposal(proposalId, requestDto, userDetails);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(responseDto);

    verify(proposalService).updateProposal(proposalId, requestDto, userDetails);
  }

  @Test
  @DisplayName("deleteProposal: 제안 삭제 성공 시 200 + 메시지 반환")
  void deleteProposal_success() {
    // given
    Long proposalId = 100L;
    CustomUserDetails userDetails = mock(CustomUserDetails.class);

    // when
    ResponseEntity<?> result =
        proposalController.deleteProposal(proposalId, userDetails);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    @SuppressWarnings("unchecked")
    Map<String, String> body = (Map<String, String>) result.getBody();

    assertThat(body)
        .containsEntry("message", "제안이 삭제되었습니다.");

    verify(proposalService).deleteProposal(proposalId, userDetails);
  }
}
