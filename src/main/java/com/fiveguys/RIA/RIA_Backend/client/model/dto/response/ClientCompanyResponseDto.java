package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClientCompanyResponseDto {
  private Long clientCompanyId;
  private String companyName;
  private Category category;
  private ClientCompany.Type type;
  private String businessNumber;
  private String phone;
  private String address;
  private String website;
  private String fax;
  private String zipCode;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
