package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySimplePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySimpleResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ClientCompanyMapper {

  // 단일 엔티티 변환
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

  // 상세 DTO
  public ClientCompanyResponseDto toDetailDto(ClientCompany entity) {
    return ClientCompanyResponseDto.builder()
        .clientCompanyId(entity.getId())
        .companyName(entity.getCompanyName())
        .category(entity.getCategory())
        .type(entity.getType())
        .businessNumber(entity.getBusinessNumber())
        .phone(entity.getPhone())
        .fax(entity.getFax())
        .address(entity.getAddress())
        .website(entity.getWebsite())
        .zipCode(entity.getZipCode())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  // 리스트 페이지 DTO
  public ClientCompanyListPageResponseDto toListPageDto(
      Page<ClientCompany> page,
      int pageNumber,
      int size
  ) {
    List<ClientCompanyListResponseDto> data = page
        .getContent()
        .stream()
        .map(this::toListDto)
        .collect(Collectors.toList());

    return ClientCompanyListPageResponseDto.builder()
        .totalCount(page.getTotalElements())
        .page(pageNumber)
        .size(size)
        .data(data)
        .build();
  }

  // 리스트 내부의 단일 요소 DTO
  public ClientCompanyListResponseDto toListDto(ClientCompany entity) {
    return ClientCompanyListResponseDto.builder()
        .clientCompanyId(entity.getId())
        .companyName(entity.getCompanyName())
        .category(entity.getCategory().name())
        .createdAt(entity.getCreatedAt())
        .build();
  }
  // 내부 조회용
  // 단일 ClientCompany 엔티티를 id, name 만 가진 단순 DTO로 변환한다
  public ClientCompanySimpleResponseDto toSimpleDto(ClientCompany entity) {
    if (entity == null) return null;

    return ClientCompanySimpleResponseDto.builder()
        .id(entity.getId())
        .name(entity.getCompanyName())
        .build();
  }

  // Page<ClientCompany> 결과를 프론트용 페이지 DTO(content, totalCount, currentPage, pageSize)로 변환한다.
  public ClientCompanySimplePageResponseDto toSimplePageDto(
      Page<ClientCompany> page,
      int currentPage,
      int pageSize
  ) {
    List<ClientCompanySimpleResponseDto> content = page.getContent().stream()
        .map(this::toSimpleDto)
        .toList();

    return ClientCompanySimplePageResponseDto.builder()
        .content(content)
        .totalCount(page.getTotalElements())
        .currentPage(currentPage)
        .pageSize(pageSize)
        .build();
  }
}