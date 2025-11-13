package com.fiveguys.RIA.RIA_Backend.client.model.service;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;

public interface ClientCompanyService {

  /**
   * 새로운 고객사 등록
   * @param dto 등록 요청 데이터
   * @return 등록된 고객사 정보
   */
  //고객사 신규 등록
  ClientCompanyResponseDto register(ClientCompanyRequestDto dto);

  //고객사 목록 조회
  ClientCompanyListPageResponseDto getCustomerCompanies(
      String keyword,
      Category category, // Enum 그대로
      int page,
      int size
  );
  //고객사 상세 조회
  ClientCompanyResponseDto getClientCompanyDetail(Long clientCompanyId);
}