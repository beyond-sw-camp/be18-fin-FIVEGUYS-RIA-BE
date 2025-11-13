package com.fiveguys.RIA.RIA_Backend.client.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientService;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ClientErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;
  private final ClientCompanyRepository clientCompanyRepository;

  // 담당자 등록
  @Override
  public ClientResponseDto register(ClientRequestDto dto) {

    ClientCompany company = clientCompanyRepository.findById(dto.getClientCompanyId())
        .orElseThrow(() -> new CustomException(ClientErrorCode.COMPANY_NOT_FOUND));

    boolean isDuplicate = clientRepository.existsByClientCompanyAndNameAndPhone(company, dto.getName(), dto.getPhone());
    if (isDuplicate) {
      throw new CustomException(ClientErrorCode.DUPLICATE_CLIENT);
    }

    Client client = Client.builder()
        .clientCompany(company)
        .name(dto.getName())
        .position(dto.getPosition())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .type(dto.getType())
        .build();

    Client saved = clientRepository.save(client);

    return ClientResponseDto.builder()
        .clientId(saved.getId())
        .clientCompanyId(company.getId())
        .name(saved.getName())
        .position(saved.getPosition())
        .email(saved.getEmail())
        .phone(saved.getPhone())
        .type(saved.getType())
        .createdAt(saved.getCreatedAt())
        .build();
  }

  // 고객사별 담당자 목록 조회
  @Override
  @Transactional(readOnly = true)
  public ClientListResponseDto getClientsByCompany(Long clientCompanyId, int page, int size) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name").ascending());
    Page<Client> result = clientRepository.findByClientCompanyIdAndIsDeletedFalse(clientCompanyId, pageable);

    return ClientListResponseDto.builder()
        .totalCount(result.getTotalElements())
        .page(page)
        .size(size)
        .data(result.getContent().stream()
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
}
