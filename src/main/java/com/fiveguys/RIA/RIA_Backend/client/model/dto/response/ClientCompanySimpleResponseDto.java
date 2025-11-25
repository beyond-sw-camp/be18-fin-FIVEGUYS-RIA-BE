package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCompanySimpleResponseDto {

  private Long id;
  private String name;
}