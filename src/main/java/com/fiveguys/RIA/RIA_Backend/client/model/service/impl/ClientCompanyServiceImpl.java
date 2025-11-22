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

  //   LEAD / CUSTOMER 등록
  @Override
  public ClientCompanyResponseDto registerLead(ClientCompanyRequestDto dto) {
    return registerInternal(dto, ClientCompany.Type.LEAD);
  }

  @Override
  public ClientCompanyResponseDto registerClient(ClientCompanyRequestDto dto) {
    return registerInternal(dto, ClientCompany.Type.CLIENT);
  }

  private ClientCompanyResponseDto registerInternal(
      ClientCompanyRequestDto dto,
      ClientCompany.Type forcedType
  ) {

    // 1. 입력값 검증
    clientCompanyValidator.validateRegister(dto);

    // 2. DTO 재조립
    ClientCompanyRequestDto fixedDto = ClientCompanyRequestDto.builder()
        .companyName(dto.getCompanyName())
        .category(dto.getCategory())
        .type(forcedType)
        .businessNumber(dto.getBusinessNumber())
        .phone(dto.getPhone())
        .fax(dto.getFax())
        .website(dto.getWebsite())
        .zipCode(dto.getZipCode())
        .address(dto.getAddress())
        .build();

    // 3. 엔티티 생성
    ClientCompany entity = clientCompanyMapper.toEntity(fixedDto);

    // 4. 저장
    ClientCompany saved = clientCompanyRepository.save(entity);

    // 5. 응답 DTO 변환
    return clientCompanyMapper.toDetailDto(saved);
  }


  // 고객사 목록 조회
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyListPageResponseDto getClientCompanies(
      String keyword,
      Category category,
      int page,
      int size
  ) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("companyName").ascending());

    Page<ClientCompany> result =
        clientCompanyLoader.loadCustomerCompanies(keyword, category, pageable);

    return clientCompanyMapper.toListPageDto(result, page, size);
  }

  // 잠재고객사 목록 조회
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyListPageResponseDto getLeadCompanies(
      String keyword,
      Category category,
      int page,
      int size
  ) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("companyName").ascending());

    Page<ClientCompany> result =
        clientCompanyLoader.loadLeadCompanies(keyword, category, pageable);

    return clientCompanyMapper.toListPageDto(result, page, size);
  }


  //   상세 조회
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyResponseDto getClientCompanyDetail(Long clientCompanyId) {

    ClientCompany company = clientCompanyLoader.loadCompany(clientCompanyId);

    return clientCompanyMapper.toDetailDto(company);
  }
}
