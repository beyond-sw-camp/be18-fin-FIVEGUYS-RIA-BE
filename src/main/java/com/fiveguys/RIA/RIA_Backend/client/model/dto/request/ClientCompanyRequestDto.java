package com.fiveguys.RIA.RIA_Backend.client.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientCompanyRequestDto {

  @NotBlank(message = "고객사명은 필수 입력값입니다.")
  private String companyName;

  @NotNull(message = "분류(CATEGORY)는 필수 입력값입니다.")
  private ClientCompany.Category category;

  @NotNull(message = "고객사 유형(TYPE)은 필수 입력값입니다.")
  private ClientCompany.Type type;

  private String businessNumber;
  private String phone;
  private String address;
  private String website;
  private String fax;
  private String zipCode;
}
