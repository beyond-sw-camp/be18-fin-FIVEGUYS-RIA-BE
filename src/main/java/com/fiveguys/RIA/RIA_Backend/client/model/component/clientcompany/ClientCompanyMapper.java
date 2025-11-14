package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ClientCompanyMapper {

  public ClientCompany toEntity(ClientCompanyRequestDto dto) {
    return ClientCompany.builder()
        .companyName(dto.getCompanyName())
        .category(dto.getCategory())
        .type(dto.getType())
        .businessNumber(dto.getBusinessNumber())
        .phone(dto.getPhone())
        .fax(dto.getFax())
        .website(dto.getWebsite())
        .zipCode(dto.getZipCode())
        .address(dto.getAddress())
        .build();
  }

  public ClientCompanyResponseDto toDetailDto(ClientCompany company) {
    return ClientCompanyResponseDto.builder()
        .clientCompanyId(company.getId())
        .companyName(company.getCompanyName())
        .category(company.getCategory())
        .type(company.getType())
        .businessNumber(company.getBusinessNumber())
        .phone(company.getPhone())
        .fax(company.getFax())
        .address(company.getAddress())
        .website(company.getWebsite())
        .zipCode(company.getZipCode())
        .createdAt(company.getCreatedAt())
        .updatedAt(company.getUpdatedAt())
        .build();
  }

  public ClientCompanyListPageResponseDto toListPageDto(
      Page<ClientCompany> page,
      int pageNumber,
      int size
  ) {
    List<ClientCompanyListResponseDto> data = page.getContent().stream()
        .map(this::toListDto)
        .collect(Collectors.toList());

    return ClientCompanyListPageResponseDto.builder()
        .totalCount(page.getTotalElements())
        .page(pageNumber)
        .size(size)
        .data(data)
        .build();
  }

  public ClientCompanyListResponseDto toListDto(ClientCompany company) {
    return ClientCompanyListResponseDto.builder()
        .clientCompanyId(company.getId())
        .companyName(company.getCompanyName())
        .category(company.getCategory().name())
        .createdAt(company.getCreatedAt())
        .build();
  }
}
