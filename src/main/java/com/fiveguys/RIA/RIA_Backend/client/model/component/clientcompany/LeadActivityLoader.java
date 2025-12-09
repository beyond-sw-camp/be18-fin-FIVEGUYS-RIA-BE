package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository.ProposalRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository.EstimateRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeadActivityLoader {

  private final ProjectRepository projectRepository;
  private final ProposalRepository proposalRepository;
  private final EstimateRepository estimateRepository;
  private final ContractRepository contractRepository;
  private final RevenueRepository revenueRepository;

  /**
   * key: clientCompanyId, value: latestActivityAt
   * 입력: key = clientCompanyId, value = 메인 담당자(Client)
   */
  public Map<Long, LocalDateTime> loadLatestActivityMap(
      Map<Long, Client> mainContactMap
  ) {
    if (mainContactMap == null || mainContactMap.isEmpty()) {
      return Map.of();
    }

    Map<Long, LocalDateTime> result = new HashMap<>();

    for (Map.Entry<Long, Client> entry : mainContactMap.entrySet()) {
      Long companyId = entry.getKey();
      Client main = entry.getValue();
      if (main == null) {
        continue;
      }

      Long clientId = main.getId();
      LocalDateTime latest = null;

      // 1) 프로젝트
      List<Project> projects =
          projectRepository.findHistoryProjectsByClient(clientId);
      for (Project p : projects) {
        latest = max(latest, p.getCreatedAt());
      }

      // 2) 제안
      List<Proposal> proposals =
          proposalRepository.findHistoryProposalsByClient(clientId);
      for (Proposal pr : proposals) {
        latest = max(latest, pr.getCreatedAt());
      }

      // 3) 견적
      List<Estimate> estimates =
          estimateRepository.findHistoryEstimatesByClient(clientId);
      for (Estimate e : estimates) {
        latest = max(latest, e.getCreatedAt());
      }

      // 4) 계약
      List<Contract> contracts =
          contractRepository.findHistoryContractsByClient(clientId);
      for (Contract c : contracts) {
        latest = max(latest, c.getCreatedAt());
      }

    }

    return result;
  }

  private LocalDateTime max(LocalDateTime a, LocalDateTime b) {
    if (a == null) return b;
    if (b == null) return a;
    return b.isAfter(a) ? b : a;
  }
}
