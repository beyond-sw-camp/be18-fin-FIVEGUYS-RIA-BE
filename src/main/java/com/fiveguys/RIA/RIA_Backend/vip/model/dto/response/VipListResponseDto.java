package com.fiveguys.RIA.RIA_Backend.vip.model.dto.response;


import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VipListResponseDto {


  private Long vipId;
  private String name;
  private String phone;
  private String grade;

}
