package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ClientCompanyContractLoader {

  private final ContractRepository contractRepository;

  public Contract loadLatestCompleted(ClientCompany company) {
    if (company == null) return null;
    return contractRepository
        .findTopByClientCompanyAndStatusOrderByContractDateDesc(
            company,
            Contract.Status.COMPLETED
        );
  }

  public Map<Long, Contract> loadLatestCompletedMap(List<ClientCompany> companies) {
    if (companies == null || companies.isEmpty()) return Map.of();

    List<Long> ids = companies.stream()
        .map(ClientCompany::getId)
        .toList();

    List<Contract> rows =
        contractRepository.findCompletedContractsByClientCompanyIds(ids);

    Map<Long, Contract> map = new HashMap<>();

    for (Contract c : rows) {
      Long companyId = c.getClientCompany().getId();
      Contract prev = map.get(companyId);

      if (prev == null || c.getContractDate().isAfter(prev.getContractDate())) {
        map.put(companyId, c);
      }
    }

    return map;
  }

  public Map<Long, Contract> noContractMap() {
    return Map.of();
  }
}
