package com.fiveguys.RIA.RIA_Backend.client.model.component.client;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ClientErrorCode;
@Component
@RequiredArgsConstructor
public class ClientValidator {

  private final ClientRepository clientRepository;

  public void validateRegister(ClientRequestDto dto, ClientCompany company) {

    if (dto.getName() == null || dto.getName().isBlank()) {
      throw new CustomException(ClientErrorCode.EMPTY_CLIENT_NAME);
    }

    if (dto.getPhone() == null || dto.getPhone().isBlank()) {
      throw new CustomException(ClientErrorCode.EMPTY_CLIENT_PHONE);
    }

    if (dto.getEmail() == null || dto.getEmail().isBlank()) {
      throw new CustomException(ClientErrorCode.EMPTY_CLIENT_EMAIL);
    }

    // 이메일 중복 체크
    boolean emailDuplicate = clientRepository.existsByEmail(dto.getEmail());
    if (emailDuplicate) {
      throw new CustomException(ClientErrorCode.DUPLICATE_CLIENT_EMAIL);
    }

    // 이름 + 전화번호 중복 체크
    boolean clientDuplicate =
        clientRepository.existsByClientCompanyAndNameAndPhone(
            company,
            dto.getName(),
            dto.getPhone()
        );

    if (clientDuplicate) {
      throw new CustomException(ClientErrorCode.DUPLICATE_CLIENT);
    }
  }
}