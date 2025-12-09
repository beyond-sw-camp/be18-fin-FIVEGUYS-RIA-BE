package com.fiveguys.RIA.RIA_Backend.campaign.revenue.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.DailySalesHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueListItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenuePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueSettlementHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.RevenueService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RevenueControllerTest {

  @Mock
  private RevenueService revenueService;

  @InjectMocks
  private RevenueController revenueController;

  @Test
  @DisplayName("getRevenueList: 매출(정산) 목록 조회 성공 시 200 + 페이지 DTO 반환")
  void getRevenueList_success() {
    // given
    int page = 1;
    int size = 20;
    String storeType = "REGULAR";
    Long creatorId = 3L;

    @SuppressWarnings("unchecked")
    RevenuePageResponseDto<RevenueListItemResponseDto> pageDto =
        (RevenuePageResponseDto<RevenueListItemResponseDto>) mock(RevenuePageResponseDto.class);

    given(revenueService.getRevenueList(eq(storeType), eq(creatorId), any(Pageable.class)))
        .willReturn(pageDto);

    // when
    ResponseEntity<RevenuePageResponseDto<RevenueListItemResponseDto>> result =
        revenueController.getRevenueList(page, size, storeType, creatorId);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(pageDto);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(revenueService).getRevenueList(eq(storeType), eq(creatorId), pageableCaptor.capture());

    Pageable captured = pageableCaptor.getValue();
    assertThat(captured.getPageNumber()).isEqualTo(page);
    assertThat(captured.getPageSize()).isEqualTo(size);
  }

  @Test
  @DisplayName("getRevenueList: 필터 없이 기본 값으로 조회 시 200 + 페이지 DTO 반환")
  void getRevenueList_withoutFilters() {
    // given
    int page = 0;
    int size = 12; // 컨트롤러 @RequestParam(defaultValue = "12")

    @SuppressWarnings("unchecked")
    RevenuePageResponseDto<RevenueListItemResponseDto> pageDto =
        (RevenuePageResponseDto<RevenueListItemResponseDto>) mock(RevenuePageResponseDto.class);

    given(revenueService.getRevenueList(eq(null), eq(null), any(Pageable.class)))
        .willReturn(pageDto);

    // when
    ResponseEntity<RevenuePageResponseDto<RevenueListItemResponseDto>> result =
        revenueController.getRevenueList(page, size, null, null);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(pageDto);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(revenueService).getRevenueList(eq(null), eq(null), pageableCaptor.capture());

    Pageable captured = pageableCaptor.getValue();
    assertThat(captured.getPageNumber()).isEqualTo(page);
    assertThat(captured.getPageSize()).isEqualTo(size);
  }

  @Test
  @DisplayName("getRevenueDetail: storeTenantMapId 포함 상세 조회 성공")
  void getRevenueDetail_withStoreTenantMapId_success() {
    // given
    Long revenueId = 10L;
    Long storeTenantMapId = 100L;

    RevenueDetailResponseDto dto = mock(RevenueDetailResponseDto.class);
    given(revenueService.getRevenueDetail(revenueId, storeTenantMapId))
        .willReturn(dto);

    // when
    ResponseEntity<RevenueDetailResponseDto> result =
        revenueController.getRevenueDetail(revenueId, storeTenantMapId);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(dto);
    verify(revenueService).getRevenueDetail(revenueId, storeTenantMapId);
  }

  @Test
  @DisplayName("getRevenueDetail: storeTenantMapId 없이 상세 조회 성공")
  void getRevenueDetail_withoutStoreTenantMapId_success() {
    // given
    Long revenueId = 10L;

    RevenueDetailResponseDto dto = mock(RevenueDetailResponseDto.class);
    given(revenueService.getRevenueDetail(revenueId, null))
        .willReturn(dto);

    // when
    ResponseEntity<RevenueDetailResponseDto> result =
        revenueController.getRevenueDetail(revenueId, null);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(dto);
    verify(revenueService).getRevenueDetail(revenueId, null);
  }

  @Test
  @DisplayName("getSettlementHistory: 계약별 정산 히스토리 조회 성공")
  void getSettlementHistory_success() {
    // given
    Long contractId = 1L;
    int startYear = 2023;
    int startMonth = 1;
    int endYear = 2024;
    int endMonth = 12;

    RevenueSettlementHistoryResponseDto dto =
        mock(RevenueSettlementHistoryResponseDto.class);

    given(revenueService.getSettlementHistoryByContractAndPeriod(
        contractId, startYear, startMonth, endYear, endMonth
    )).willReturn(dto);

    // when
    RevenueSettlementHistoryResponseDto result =
        revenueController.getSettlementHistory(
            contractId, startYear, startMonth, endYear, endMonth
        );

    // then
    assertThat(result).isSameAs(dto);
    verify(revenueService).getSettlementHistoryByContractAndPeriod(
        contractId, startYear, startMonth, endYear, endMonth
    );
  }

  @Test
  @DisplayName("getDailySalesHistory: 매장 일별 매출 히스토리 조회 성공")
  void getDailySalesHistory_success() {
    // given
    Long storeTenantMapId = 10L;
    LocalDate startDate = LocalDate.of(2024, 1, 1);
    LocalDate endDate = LocalDate.of(2024, 1, 31);

    DailySalesHistoryResponseDto dto = mock(DailySalesHistoryResponseDto.class);

    given(revenueService.getDailySalesHistory(storeTenantMapId, startDate, endDate))
        .willReturn(dto);

    // when
    DailySalesHistoryResponseDto result =
        revenueController.getDailySalesHistory(storeTenantMapId, startDate, endDate);

    // then
    assertThat(result).isSameAs(dto);
    verify(revenueService).getDailySalesHistory(storeTenantMapId, startDate, endDate);
  }

  // 간단 mock 헬퍼 (제네릭 경고 최소화용)
  @SuppressWarnings("unchecked")
  private <T> T mock(Class<T> clazz) {
    return org.mockito.Mockito.mock(clazz);
  }
}
