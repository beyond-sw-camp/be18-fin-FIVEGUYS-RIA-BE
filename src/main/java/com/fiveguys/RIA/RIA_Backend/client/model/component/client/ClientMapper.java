package com.fiveguys.RIA.RIA_Backend.client.model.component.client;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientSimplePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientSimpleResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

  public Client toEntity(ClientRequestDto dto, ClientCompany company) {
    return Client.builder()
        .clientCompany(company)
        .name(dto.getName())
        .position(dto.getPosition())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .type(company.getType())
        .build();
  }

  public ClientResponseDto toResponseDto(Client client) {
    return ClientResponseDto.builder()
        .clientId(client.getId())
        .clientCompanyId(client.getClientCompany().getId())
        .name(client.getName())
        .position(client.getPosition())
        .email(client.getEmail())
        .phone(client.getPhone())
        .type(client.getType())
        .createdAt(client.getCreatedAt())
        .build();
  }

  public ClientListResponseDto toListResponseDto(Page<Client> page, Long clientCompanyId) {
    return ClientListResponseDto.builder()
        .totalCount(page.getTotalElements())
        .page(page.getNumber() + 1)
        .size(page.getSize())
        .data(page.getContent().stream()
            .map(client -> ClientResponseDto.builder()
                .clientId(client.getId())
                .clientCompanyId(clientCompanyId)
                .name(client.getName())
                .position(client.getPosition())
                .email(client.getEmail())
                .phone(client.getPhone())
                .type(client.getType())
                .createdAt(client.getCreatedAt())
                .build())
            .collect(Collectors.toList()))
        .build();
  }

  public ClientSimpleResponseDto toSimpleDto(Client client) {
    if (client == null) return null;

    return ClientSimpleResponseDto.builder()
        .id(client.getId())
        .name(client.getName())
        .build();
  }

  // Page<Client> 결과를 simple 페이지 DTO로 변환
  public ClientSimplePageResponseDto toSimplePageDto(
      Page<Client> page,
      int currentPage,
      int pageSize
  ) {
    List<ClientSimpleResponseDto> content = page.getContent().stream()
        .map(this::toSimpleDto)
        .toList();

    return ClientSimplePageResponseDto.builder()
        .content(content)
        .totalCount(page.getTotalElements())
        .currentPage(currentPage)
        .pageSize(pageSize)
        .build();
  }
}