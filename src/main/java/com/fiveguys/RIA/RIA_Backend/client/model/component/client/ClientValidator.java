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

    if (dto.getType() == null) {
      throw new CustomException(ClientErrorCode.EMPTY_CLIENT_TYPE);
    }

    boolean isDuplicate =
        clientRepository.existsByClientCompanyAndNameAndPhone(
            company,
            dto.getName(),
            dto.getPhone()
        );

    if (isDuplicate) {
      throw new CustomException(ClientErrorCode.DUPLICATE_CLIENT);
    }
  }
}