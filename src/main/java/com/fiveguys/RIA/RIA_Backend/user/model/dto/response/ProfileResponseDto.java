package com.fiveguys.RIA.RIA_Backend.user.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProfileResponseDto {
  private String department;
  private String email;
  private String employeeId;
  private String name;
  private String position;

}
