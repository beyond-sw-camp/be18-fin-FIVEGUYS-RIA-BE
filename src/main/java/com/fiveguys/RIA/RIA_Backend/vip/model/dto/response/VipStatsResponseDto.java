package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipStatsResponseDto {

  private long total;

  private long psrBlack;
  private long psrWhite;

  private long parkJadeBlack;
  private long parkJadeWhite;
  private long parkJadeBlue;

  private long jadePlus;
  private long jade;
}