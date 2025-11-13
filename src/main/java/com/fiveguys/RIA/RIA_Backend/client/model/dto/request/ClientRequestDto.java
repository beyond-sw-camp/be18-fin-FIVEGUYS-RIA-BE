package com.fiveguys.RIA.RIA_Backend.client.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientRequestDto {

  @NotNull(message = "고객사 ID는 필수값입니다.")
  private Long clientCompanyId;

  @NotBlank(message = "담당자 이름은 필수값입니다.")
  private String name;

  private String position;

  @NotBlank(message = "이메일은 필수값입니다.")
  private String email;

  private String phone;

  @NotNull(message = "담당자 유형은 필수값입니다.")
  private Client.Type type;

  private String note;
}
