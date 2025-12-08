package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientCompanyLeadSummaryLoader {

  private final ClientRepository clientRepository;

  /**
   * 잠재 고객사 카드에 쓸 “대표 담당자” 맵.
   * - 한 고객사에 담당자가 여러 명이면 가장 최근에 생성된 사람 1명만 사용.
   */
  public Map<Long, Client> loadMainContacts(List<ClientCompany> companies) {
    if (companies == null || companies.isEmpty()) {
      return Map.of();
    }

    List<Long> ids = companies.stream()
        .map(ClientCompany::getId)
        .toList();

    List<Client> rows = clientRepository.findLatestByCompanyIds(ids);

    Map<Long, Client> result = new HashMap<>();
    for (Client c : rows) {
      Long companyId = c.getClientCompany().getId();
      // 쿼리에서 companyId asc, createdAt desc 로 정렬해놨다고 가정 → 첫 row 만 사용
      if (!result.containsKey(companyId)) {
        result.put(companyId, c);
      }
    }
    return result;
  }
}
