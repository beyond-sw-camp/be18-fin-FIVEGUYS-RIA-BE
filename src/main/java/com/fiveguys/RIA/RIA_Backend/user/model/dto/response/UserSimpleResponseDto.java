package com.fiveguys.RIA.RIA_Backend.user.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSimpleResponseDto {

  private final Long userId;
  private final String name;
}