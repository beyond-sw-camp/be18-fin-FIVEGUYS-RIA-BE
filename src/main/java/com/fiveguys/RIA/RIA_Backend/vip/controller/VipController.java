package com.fiveguys.RIA.RIA_Backend.vip.controller;

import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.service.VipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vip")
public class VipController {

  private final VipService vipService;

  @GetMapping
  public ResponseEntity<VipListPageResponseDto> getVipList(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "grade", required = false) Vip.VipGrade grade

  ) {
    return ResponseEntity.ok(vipService.getVipList(page, size, grade));
  }

  @GetMapping("/stats")
  public ResponseEntity<VipStatsResponseDto> getVipStats() {
    return ResponseEntity.ok(vipService.getStats());
  }
}
