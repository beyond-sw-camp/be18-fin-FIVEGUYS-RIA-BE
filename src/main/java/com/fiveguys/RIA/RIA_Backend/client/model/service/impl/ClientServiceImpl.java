package com.fiveguys.RIA.RIA_Backend.client.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.client.model.component.client.ClientLoader;
import com.fiveguys.RIA.RIA_Backend.client.model.component.client.ClientMapper;
import com.fiveguys.RIA.RIA_Backend.client.model.component.client.ClientValidator;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

  private final ClientLoader clientLoader;
  private final ClientValidator clientValidator;
  private final ClientMapper clientMapper;
  private final ClientRepository clientRepository;

  // 담당자 등록
  @Override
  public ClientResponseDto register(ClientRequestDto dto) {

    // 1. 회사 로딩
    ClientCompany company = clientLoader.loadCompany(dto.getClientCompanyId());

    // 2. 검증 (필수값 + 중복)
    clientValidator.validateRegister(dto, company);

    // 3. 엔티티 생성
    Client client = clientMapper.toEntity(dto, company);

    // 4. 저장
    Client saved = clientRepository.save(client);

    // 5. 응답 DTO 변환
    return clientMapper.toResponseDto(saved);
  }

  // 고객사별 담당자 목록 조회
  @Override
  @Transactional(readOnly = true)
  public ClientListResponseDto getClientsByCompany(Long clientCompanyId, int page, int size) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name").ascending());

    Page<Client> result = clientLoader.loadClientsByCompany(clientCompanyId, pageable);

    return clientMapper.toListResponseDto(result, clientCompanyId);
  }
}