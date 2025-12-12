package com.fiveguys.RIA.RIA_Backend.client.model.component.client;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ClientErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientLoader {

  private final ClientCompanyRepository clientCompanyRepository;
  private final ClientRepository clientRepository;

  public ClientCompany loadCompany(Long clientCompanyId) {
    return clientCompanyRepository.findById(clientCompanyId)
        .orElseThrow(() -> new CustomException(ClientErrorCode.COMPANY_NOT_FOUND));
  }

  public Page<Client> loadClientsByCompany(Long clientCompanyId, Pageable pageable) {
    return clientRepository.findByClientCompanyIdAndIsDeletedFalse(clientCompanyId, pageable);
  }

  public Page<Client> loadClientsByCompany(Long clientCompanyId, String keyword, Pageable pageable) {
    if (keyword == null || keyword.isBlank()) {
      return clientRepository.findByClientCompanyIdAndIsDeletedFalse(clientCompanyId, pageable);
    }
    return clientRepository.searchByCompanyAndName(clientCompanyId, keyword, pageable);
  }
}
