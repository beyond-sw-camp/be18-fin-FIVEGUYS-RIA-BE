package com.fiveguys.RIA.RIA_Backend.client.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.client.model.component.client.ClientLoader;
import com.fiveguys.RIA.RIA_Backend.client.model.component.client.ClientMapper;
import com.fiveguys.RIA.RIA_Backend.client.model.component.client.ClientValidator;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientSimplePageResponseDto;
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
  public ClientResponseDto register(Long clientCompanyId, ClientRequestDto dto) {

    // 1. 회사 로딩
    ClientCompany company = clientLoader.loadCompany(clientCompanyId);

    // 2. 검증
    clientValidator.validateRegister(dto, company);

    // 3. 엔티티 생성 (type은 company.getType() 그대로 들어간다)
    Client client = clientMapper.toEntity(dto, company);

    // 4. 저장
    Client saved = clientRepository.save(client);

    // 5. 응답 반환
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

  @Override
  @Transactional(readOnly = true)
  public ClientSimplePageResponseDto getSimpleClientsByCompany(
      Long clientCompanyId,
      String keyword,
      int page,
      int size
  ) {
    // 검색어 정제
    String kw = clientValidator.normalizeKeyword(keyword);

    // 페이지 보정 (0 기반 index)
    int pageIndex = (page <= 0) ? 0 : page - 1;
    int pageSize = (size <= 0) ? 10 : size;

    Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("name").ascending());

    // 회사 존재 여부 검증 (없으면 COMPANY_NOT_FOUND 예외)
    clientLoader.loadCompany(clientCompanyId);

    // 담당자 목록 로딩 (회사 + keyword)
    Page<Client> result = clientLoader.loadClientsByCompany(clientCompanyId, kw, pageable);

    // DTO 변환
    return clientMapper.toSimplePageDto(result, page, pageSize);
  }
}