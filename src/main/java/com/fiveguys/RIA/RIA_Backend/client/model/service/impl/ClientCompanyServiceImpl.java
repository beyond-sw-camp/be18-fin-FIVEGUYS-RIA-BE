package com.fiveguys.RIA.RIA_Backend.client.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
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

    if (clientCompanyRepository.existsByCompanyName(dto.getCompanyName())) {
      throw new CustomException(ClientErrorCode.DUPLICATE_COMPANY);
    }

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
      String keyword, ClientCompany.Category category, int page, int size) {

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
