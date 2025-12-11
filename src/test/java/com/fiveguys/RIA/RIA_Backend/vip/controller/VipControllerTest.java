package com.fiveguys.RIA.RIA_Backend.vip.controller;

import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipBrandTop5ResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipGradeStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipSalesTrendResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStoreSalesPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.service.VipService;
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
class VipControllerTest {

  @Mock
  private VipService vipService;

  @InjectMocks
  private VipController vipController;

  @Test
  @DisplayName("getVipList: VIP 목록 조회 성공 시 200 + 페이지 DTO 반환")
  void getVipList_success() {
    // given
    int page = 1;
    int size = 10;
    Vip.VipGrade grade = null; // 등급 필터 미사용 케이스
    String keyword = "검색어";

    VipListPageResponseDto pageDto = mock(VipListPageResponseDto.class);

    given(vipService.getVipList(page, size, grade, keyword))
        .willReturn(pageDto);

    // when
    ResponseEntity<VipListPageResponseDto> result =
        vipController.getVipList(page, size, grade, keyword);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(pageDto);

    verify(vipService).getVipList(page, size, grade, keyword);
  }

  @Test
  @DisplayName("getVipStats: VIP 통계 조회 성공 시 200 + 통계 DTO 반환")
  void getVipStats_success() {
    // given
    VipGradeStatsResponseDto dto = mock(VipGradeStatsResponseDto.class);
    given(vipService.getStats()).willReturn(dto);

    // when
    ResponseEntity<VipGradeStatsResponseDto> result =
        vipController.getVipStats();

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isSameAs(dto);

    verify(vipService).getStats();
  }

  @Test
  @DisplayName("getVipSalesStats: VIP 매출 요약 통계 조회 성공")
  void getVipSalesStats_success() {
    // given
    Integer year = 2024;
    Integer month = 12;

    VipStatsResponseDto dto = mock(VipStatsResponseDto.class);
    given(vipService.getVipSalesStats(year, month)).willReturn(dto);

    // when
    VipStatsResponseDto result =
        vipController.getVipSalesStats(year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(vipService).getVipSalesStats(year, month);
  }

  @Test
  @DisplayName("getVipBrandTop5: VIP 매출 상위 브랜드 TOP5 조회 성공")
  void getVipBrandTop5_success() {
    // given
    Integer year = 2024;
    Integer month = 12;

    VipBrandTop5ResponseDto dto = mock(VipBrandTop5ResponseDto.class);
    given(vipService.getVipBrandTop5(year, month)).willReturn(dto);

    // when
    VipBrandTop5ResponseDto result =
        vipController.getVipBrandTop5(year, month);

    // then
    assertThat(result).isSameAs(dto);
    verify(vipService).getVipBrandTop5(year, month);
  }

  @Test
  @DisplayName("getVipSalesTrend: VIP 매출 추이 조회 성공")
  void getVipSalesTrend_success() {
    // given
    VipSalesTrendResponseDto dto = mock(VipSalesTrendResponseDto.class);
    given(vipService.getVipSalesTrend()).willReturn(dto);

    // when
    VipSalesTrendResponseDto result =
        vipController.getVipSalesTrend();

    // then
    assertThat(result).isSameAs(dto);
    verify(vipService).getVipSalesTrend();
  }

  @Test
  @DisplayName("getVipStoreSalesPage: 매장별 VIP 매출 현황 조회 성공")
  void getVipStoreSalesPage_success() {
    // given
    Integer year = 2024;
    Integer month = 12;
    int page = 0;
    int size = 5;

    VipStoreSalesPageResponseDto dto = mock(VipStoreSalesPageResponseDto.class);
    given(vipService.getVipStoreSalesPage(year, month, page, size))
        .willReturn(dto);

    // when
    VipStoreSalesPageResponseDto result =
        vipController.getVipStoreSalesPage(year, month, page, size);

    // then
    assertThat(result).isSameAs(dto);
    verify(vipService).getVipStoreSalesPage(year, month, page, size);
  }
}
