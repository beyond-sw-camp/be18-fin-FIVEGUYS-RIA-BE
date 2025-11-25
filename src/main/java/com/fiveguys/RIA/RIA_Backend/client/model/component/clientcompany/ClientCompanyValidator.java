package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ClientErrorCode;
import java.util.Locale;
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

  // 문자열 type → enum 변환- null / 공백 / "ALL"  → null (필터 없음)- "CLIENT" / "LEAD" → 해당 enum- 그 외 이상한 값 → null (필터 없음으로 처리)
  public ClientCompany.Type parseType(String type) {
    if (type == null || type.isBlank()) return null;

    String upper = type.toUpperCase(Locale.ROOT);

    if ("ALL".equals(upper)) {
      return null;
    }

    try {
      return ClientCompany.Type.valueOf(upper); // LEAD, CLIENT
    } catch (Exception e) {
      return null;
    }
  }

  //keyword 공백 처리 - null / 공백 → null - 나머지 → trim 해서 그대로
  public String normalizeKeyword(String keyword) {
    if (keyword == null) return null;
    String trimmed = keyword.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }
}
