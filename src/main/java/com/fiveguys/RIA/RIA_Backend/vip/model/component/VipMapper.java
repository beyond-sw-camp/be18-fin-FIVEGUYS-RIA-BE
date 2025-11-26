package com.fiveguys.RIA.RIA_Backend.vip.model.component;

import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class VipMapper {

  public VipListResponseDto toListDto(Vip v) {
    return VipListResponseDto.builder()
        .vipId(v.getId())
        .name(v.getName())
        .phone(v.getPhone())
        .grade(v.getGrade().name())
        .build();
  }

  public VipListPageResponseDto toListPageDto(Page<Vip> result, int requestPage, int size) {
    return VipListPageResponseDto.builder()
        .vips(result.getContent().stream()
            .map(this::toListDto)
            .toList())
        // 프론트 기준 1-based page
        .page(requestPage)
        .size(size)
        .totalElements(result.getTotalElements())  // Page 메타데이터 그대로 사용
        .totalPages(result.getTotalPages())        // 직접 계산 금지
        .last(result.isLast())
        .build();
  }

  public VipStatsResponseDto toStatsDto(
      long total,
      long psrBlack,
      long psrWhite,
      long parkJadeBlack,
      long parkJadeWhite,
      long parkJadeBlue,
      long jadePlus,
      long jade
  ) {
    return VipStatsResponseDto.builder()
        .total(total)
        .psrBlack(psrBlack)
        .psrWhite(psrWhite)
        .parkJadeBlack(parkJadeBlack)
        .parkJadeWhite(parkJadeWhite)
        .parkJadeBlue(parkJadeBlue)
        .jadePlus(jadePlus)
        .jade(jade)
        .build();
  }
}