package com.fiveguys.RIA.RIA_Backend.campaign.Estimate.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.controller.EstimateController;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.*;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.service.EstimateService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EstimateControllerTest {

    @Mock
    private EstimateService estimateService;

    @InjectMocks
    private EstimateController estimateController;

    @Test
    @DisplayName("createEstimate: 견적 생성 성공 시 200 + 응답 DTO 반환")
    void createEstimate_success() {
        CustomUserDetails user = mock(CustomUserDetails.class);
        given(user.getUserId()).willReturn(5L);

        EstimateCreateRequestDto req = mock(EstimateCreateRequestDto.class);
        EstimateCreateResponseDto resDto = mock(EstimateCreateResponseDto.class);

        given(estimateService.createEstimate(req, 5L))
                .willReturn(resDto);

        ResponseEntity<EstimateCreateResponseDto> result =
                estimateController.createEstimate(user, req);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(resDto);
        verify(estimateService).createEstimate(req, 5L);
    }

    @Test
    @DisplayName("getEstimates: 필터 + 페이지로 견적 목록 조회 성공")
    void getEstimates_success() {
        Long projectId = 1L;
        Long companyId = 2L;
        String keyword = "test";
        Estimate.Status status = Estimate.Status.DRAFT;
        int page = 1;
        int size = 20;

        EstimatePageResponseDto<EstimateListResponseDto> pageDto =
                mock(EstimatePageResponseDto.class);

        given(estimateService.getEstimateList(
                projectId,
                companyId,
                keyword,
                status,
                page,
                size
        )).willReturn(pageDto);

        ResponseEntity<?> result =
                estimateController.getEstimates(
                        projectId,
                        companyId,
                        keyword,
                        status,
                        page,
                        size
                );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(pageDto);
        verify(estimateService)
                .getEstimateList(projectId, companyId, keyword, status, page, size);
    }

    @Test
    @DisplayName("getEstimateDetail: 견적 상세 조회 성공")
    void getEstimateDetail_success() {
        Long estimateId = 10L;

        EstimateDetailResponseDto detailDto = mock(EstimateDetailResponseDto.class);

        given(estimateService.getEstimateDetail(estimateId))
                .willReturn(detailDto);

        ResponseEntity<EstimateDetailResponseDto> result =
                estimateController.getEstimateDetail(estimateId);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(detailDto);
        verify(estimateService).getEstimateDetail(estimateId);
    }

    @Test
    @DisplayName("deleteEstimate: 견적 삭제 성공 시 200 + 삭제 응답 반환")
    void deleteEstimate_success() {
        Long estimateId = 99L;

        CustomUserDetails user = mock(CustomUserDetails.class);
        EstimateDeleteResponseDto deleteDto = mock(EstimateDeleteResponseDto.class);

        given(estimateService.deleteEstimate(estimateId, user))
                .willReturn(deleteDto);

        ResponseEntity<EstimateDeleteResponseDto> result =
                estimateController.deleteEstimate(estimateId, user);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(deleteDto);
        verify(estimateService).deleteEstimate(estimateId, user);
    }

    @Test
    @DisplayName("updateEstimate: 견적 수정 성공 시 200 + 수정된 DTO 반환")
    void updateEstimate_success() {
        Long estimateId = 33L;

        EstimateUpdateRequestDto updateDto = mock(EstimateUpdateRequestDto.class);
        CustomUserDetails user = mock(CustomUserDetails.class);

        EstimateDetailResponseDto updatedDto = mock(EstimateDetailResponseDto.class);

        given(estimateService.updateEstimate(estimateId, updateDto, user))
                .willReturn(updatedDto);

        ResponseEntity<EstimateDetailResponseDto> result =
                estimateController.updateEstimate(estimateId, updateDto, user);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(updatedDto);
        verify(estimateService).updateEstimate(estimateId, updateDto, user);
    }
}
