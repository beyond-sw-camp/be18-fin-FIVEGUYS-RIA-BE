package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipListPageResponseDto {

  private List<VipListResponseDto> vips;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
  private boolean last;

}
