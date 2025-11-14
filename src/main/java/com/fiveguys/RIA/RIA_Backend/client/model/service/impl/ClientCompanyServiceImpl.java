package com.fiveguys.RIA.RIA_Backend.client.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanyLoader;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanyMapper;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanyValidator;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientCompanyServiceImpl implements ClientCompanyService {

  private final ClientCompanyRepository clientCompanyRepository;
  private final ClientCompanyLoader clientCompanyLoader;
  private final ClientCompanyValidator clientCompanyValidator;
  private final ClientCompanyMapper clientCompanyMapper;

  // 회사 등록
  @Override
  public ClientCompanyResponseDto register(ClientCompanyRequestDto dto) {

    // 1. 입력 검증
    clientCompanyValidator.validateRegister(dto);

    // 2. 엔티티 생성
    ClientCompany company = clientCompanyMapper.toEntity(dto);

    // 3. 저장
    ClientCompany saved = clientCompanyRepository.save(company);

    // 4. 응답 DTO
    return clientCompanyMapper.toDetailDto(saved);
  }

  // 목록 조회
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyListPageResponseDto getCustomerCompanies(
      String keyword, Category category, int page, int size) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("companyName").ascending());

    Page<ClientCompany> result =
        clientCompanyLoader.loadCustomerCompanies(keyword, category, pageable);

    return clientCompanyMapper.toListPageDto(result, page, size);
  }

  // 상세 조회
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyResponseDto getClientCompanyDetail(Long clientCompanyId) {

    ClientCompany company = clientCompanyLoader.loadCompany(clientCompanyId);

    return clientCompanyMapper.toDetailDto(company);
  }
}