package com.fiveguys.RIA.RIA_Backend.client.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientRequestDto {

  @NotBlank(message = "담당자명은 필수입니다.")
  private String name;

  private String position;

  @NotBlank(message = "전화번호는 필수입니다.")
  private String phone;

  private String email;
}
