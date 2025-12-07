package com.fiveguys.RIA.RIA_Backend.campaign.revenue.controller;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueListItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenuePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/revenues")
@RequiredArgsConstructor
public class RevenueController {

  private final RevenueService revenueService;

  /**
   * 매출(정산) 목록 조회
   * <p>
   * 필터 - storeType   : STORE.type (예: REGULAR / POPUP) - creatorId   : REVENUE.created_user (담당자)
   * <p>
   * 페이지네이션 - page : 0-base - size : 페이지당 카드 수
   */
  @GetMapping
  public ResponseEntity<RevenuePageResponseDto<RevenueListItemResponseDto>> getRevenueList(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "12") int size,
      @RequestParam(name = "storeType", required = false) String storeType,
      @RequestParam(name = "creatorId", required = false) Long creatorId
  ) {
    Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(
        revenueService.getRevenueList(storeType, creatorId, pageable)
    );
  }
}