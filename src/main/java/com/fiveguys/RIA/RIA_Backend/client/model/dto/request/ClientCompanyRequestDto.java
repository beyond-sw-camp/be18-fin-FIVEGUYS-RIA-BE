package com.fiveguys.RIA.RIA_Backend.client.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ClientCompanyRequestDto {

  private String companyName;
  private Category category;
  private ClientCompany.Type type;

  private String businessNumber;
  private String phone;
  private String address;
  private String website;
  private String fax;
  private String zipCode;
}
