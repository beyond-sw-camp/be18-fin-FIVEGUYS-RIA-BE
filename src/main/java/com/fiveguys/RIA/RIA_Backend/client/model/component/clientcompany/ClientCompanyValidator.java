package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ClientErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientCompanyValidator {

  private final ClientCompanyRepository clientCompanyRepository;

  public void validateRegister(ClientCompanyRequestDto dto) {

    // 필수값
    if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) {
      throw new CustomException(ClientErrorCode.EMPTY_COMPANY_NAME);
    }
    if (dto.getCategory() == null) {
      throw new CustomException(ClientErrorCode.EMPTY_CATEGORY);
    }

    // 중복 회사명
    if (clientCompanyRepository.existsByCompanyName(dto.getCompanyName())) {
      throw new CustomException(ClientErrorCode.DUPLICATE_COMPANY);
    }

    // 중복 사업자번호 (NULL 아닐 때만)
    if (dto.getBusinessNumber() != null &&
        clientCompanyRepository.existsByBusinessNumber(dto.getBusinessNumber())) {
      throw new CustomException(ClientErrorCode.DUPLICATE_BUSINESS_NUMBER);
    }

    // 중복 홈페이지 (NULL 아닐 때만)
    if (dto.getWebsite() != null &&
        clientCompanyRepository.existsByWebsite(dto.getWebsite())) {
      throw new CustomException(ClientErrorCode.DUPLICATE_WEBSITE);
    }
  }
}