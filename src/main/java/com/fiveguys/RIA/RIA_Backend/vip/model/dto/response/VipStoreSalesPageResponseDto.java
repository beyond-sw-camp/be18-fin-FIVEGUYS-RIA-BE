package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipStoreSalesPageResponseDto {

  private int year;
  private int month;

  private int page;          // 0-based
  private int size;          // 요청 size
  private long totalElements;
  private int totalPages;

  private List<VipStoreSalesItemResponseDto> items;
}
