package com.fiveguys.RIA.RIA_Backend.client.model.service;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;

public interface ClientCompanyService {

  //고객사 신규 등록
  ClientCompanyResponseDto registerCustomer(ClientCompanyRequestDto dto);

  //잠재 고객사 신규 등록
  ClientCompanyResponseDto registerLead(ClientCompanyRequestDto dto);

  //고객사 목록 조회
  ClientCompanyListPageResponseDto getCustomerCompanies(
      String keyword,
      Category category, // Enum 그대로
      int page,
      int size
  );

  ClientCompanyListPageResponseDto getLeadCompanies(
      String keyword,
      Category category, // Enum 그대로
      int page,
      int size
  );

  //고객사 상세 조회
  ClientCompanyResponseDto getClientCompanyDetail(Long clientCompanyId);
}