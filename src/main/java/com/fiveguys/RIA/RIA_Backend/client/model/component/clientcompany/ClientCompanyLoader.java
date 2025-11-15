package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ClientErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientCompanyLoader {

  private final ClientCompanyRepository clientCompanyRepository;

  public ClientCompany loadCompany(Long id) {
    return clientCompanyRepository.findById(id)
        .orElseThrow(() -> new CustomException(ClientErrorCode.COMPANY_NOT_FOUND));
  }

  public Page<ClientCompany> loadCustomerCompanies(String keyword, Category category, Pageable pageable) {
    return clientCompanyRepository.findCustomerCompanies(keyword, category, pageable);
  }

  public Page<ClientCompany> loadLeadCompanies(String keyword, Category category, Pageable pageable) {
    return clientCompanyRepository.findLeadCompanies(keyword, category, pageable);
  }
}
