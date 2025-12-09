package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipBrandTop5ResponseDto {

  private int year;
  private int month;
  private List<VipBrandTopItemResponseDto> items;
}
