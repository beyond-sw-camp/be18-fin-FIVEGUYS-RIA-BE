package com.fiveguys.RIA.RIA_Backend.client.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientCompanyService;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ClientErrorCode;
import java.util.List;
import java.util.stream.Collectors;
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

  //회사 등록
  @Override
  public ClientCompanyResponseDto register(ClientCompanyRequestDto dto) {

    // 1. 필수값 검증
    if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) {
      throw new CustomException(ClientErrorCode.EMPTY_COMPANY_NAME);
    }
    if (dto.getCategory() == null) {
      throw new CustomException(ClientErrorCode.EMPTY_CATEGORY);
    }
    if (dto.getType() == null) {
      throw new CustomException(ClientErrorCode.EMPTY_TYPE);
    }

    // 2. 중복 회사명
    if (clientCompanyRepository.existsByCompanyName(dto.getCompanyName())) {
      throw new CustomException(ClientErrorCode.DUPLICATE_COMPANY);
    }

    // 3. 중복 사업자번호 (NULL 아닐 때만 검사)
    if (dto.getBusinessNumber() != null &&
        clientCompanyRepository.existsByBusinessNumber(dto.getBusinessNumber())) {
      throw new CustomException(ClientErrorCode.DUPLICATE_BUSINESS_NUMBER);
    }

    // 4. 중복 홈페이지 주소 (NULL 아닐 때만 검사)
    if (dto.getWebsite() != null &&
        clientCompanyRepository.existsByWebsite(dto.getWebsite())) {
      throw new CustomException(ClientErrorCode.DUPLICATE_WEBSITE);
    }

    // 5. 엔티티 생성
    ClientCompany company = ClientCompany.builder()
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

    ClientCompany saved = clientCompanyRepository.save(company);

    // 6. DTO 응답 변환
    return ClientCompanyResponseDto.builder()
        .clientCompanyId(saved.getId())
        .companyName(saved.getCompanyName())
        .category(saved.getCategory())
        .type(saved.getType())
        .businessNumber(saved.getBusinessNumber())
        .phone(saved.getPhone())
        .fax(saved.getFax())
        .website(saved.getWebsite())
        .zipCode(saved.getZipCode())
        .address(saved.getAddress())
        .createdAt(saved.getCreatedAt())
        .updatedAt(saved.getUpdatedAt())
        .build();
  }


  //목록조회
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyListPageResponseDto getCustomerCompanies(
      String keyword, Category category, int page, int size) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("companyName").ascending());

    Page<ClientCompany> result = clientCompanyRepository.findCustomerCompanies(keyword, category, pageable);

    List<ClientCompanyListResponseDto> data = result.getContent().stream()
        .map(company -> ClientCompanyListResponseDto.builder()
            .clientCompanyId(company.getId())
            .companyName(company.getCompanyName())
            .category(company.getCategory().name())
            .createdAt(company.getCreatedAt())
            .build())
        .collect(Collectors.toList());

    return ClientCompanyListPageResponseDto.builder()
        .totalCount(result.getTotalElements())
        .page(page)
        .size(size)
        .data(data)
        .build();
  }


  //상세 조회
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyResponseDto getClientCompanyDetail(Long clientCompanyId) {
    ClientCompany company = clientCompanyRepository.findById(clientCompanyId)
        .orElseThrow(() -> new CustomException(ClientErrorCode.COMPANY_NOT_FOUND));

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
}
