package com.fiveguys.RIA.RIA_Backend.vip.model.component;

import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipGradeStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class VipMapper {

  private static final DateTimeFormatter DATE_FORMATTER =
          DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public VipListResponseDto toListDto(Vip v, Long totalSales) {
    return VipListResponseDto.builder()
                             .vipId(v.getId())
                             .name(v.getName())
                             .phone(v.getPhone())
                             .grade(v.getGrade().name())
                             .createdAt(
                                     v.getCreatedAt() != null
                                             ? v.getCreatedAt().format(DATE_FORMATTER)
                                             : "-"
                             )
                             .totalSales(totalSales != null ? totalSales : 0L)
                             .build();
  }

  public VipListResponseDto toListDto(Vip v) {
    return toListDto(v, 0L);
  }

  // AiService 처럼, 미리 content(List<VipListResponseDto>) 만들어서 넘기는 경우
  public VipListPageResponseDto toListPageDto(
          Page<Vip> result,
          int requestPage,
          int size,
          List<VipListResponseDto> content
  ) {
    return VipListPageResponseDto.builder()
                                 .vips(content)
                                 .page(requestPage)
                                 .size(size)
                                 .totalElements(result.getTotalElements())
                                 .totalPages(result.getTotalPages())
                                 .last(result.isLast())
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

  public VipGradeStatsResponseDto toStatsDto(
      long total,
      long psrBlack,
      long psrWhite,
      long parkJadeBlack,
      long parkJadeWhite,
      long parkJadeBlue,
      long jadePlus,
      long jade
  ) {
    return VipGradeStatsResponseDto.builder()
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
