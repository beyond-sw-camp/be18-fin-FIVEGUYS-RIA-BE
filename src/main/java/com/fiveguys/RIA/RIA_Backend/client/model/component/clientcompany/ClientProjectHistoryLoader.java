package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository.EstimateRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository.ProposalRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.RevenueRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientProjectHistoryLoader {

  private final ProjectRepository projectRepository;
  private final ProposalRepository proposalRepository;
  private final EstimateRepository estimateRepository;
  private final ContractRepository contractRepository;
  private final RevenueRepository revenueRepository;  // 여기만 교체

  public ClientProjectHistoryResponseDto load(Long clientId) {

    List<Project> projects =
        projectRepository.findHistoryProjectsByClient(clientId);

    List<ClientProjectHistoryItemResponseDto> projectDtos = projects.stream()
        .map(this::toProjectHistoryItem)
        .collect(Collectors.toList());

    return ClientProjectHistoryResponseDto.builder()
        .clientId(clientId)
        .projects(projectDtos)
        .build();
  }

  private ClientProjectHistoryItemResponseDto toProjectHistoryItem(Project project) {

    String managerName =
        project.getSalesManager() != null ? project.getSalesManager().getName() : "-";

    List<ProjectTimelineEventResponseDto> events = new ArrayList<>();

    // 프로젝트 생성
    events.add(ProjectTimelineEventResponseDto.builder()
        .type("PROJECT")
        .title("[프로젝트 생성] " + nvl(project.getTitle()))
        .content(nvl(project.getDescription()))
        .owner(managerName)
        .occurredAt(project.getCreatedAt())
        .build());

    // 제안
    for (Proposal pr : proposalRepository.findByProjectForHistory(project)) {
      events.add(ProjectTimelineEventResponseDto.builder()
          .type("PROPOSAL")
          .title("[제안] " + nvl(pr.getTitle()))
          .content(nvl(pr.getRemark()))
          .owner(managerName)
          .occurredAt(pr.getCreatedAt())
          .build());
    }

    // 견적
    for (Estimate e : estimateRepository.findByProjectForHistory(project)) {
      events.add(ProjectTimelineEventResponseDto.builder()
          .type("ESTIMATE")
          .title("[견적] " + nvl(e.getEstimateTitle()))
          .content("비고: " + e.getRemark())
          .owner(managerName)
          .occurredAt(e.getCreatedAt())
          .build());
    }

    // 계약
    for (Contract c : contractRepository.findByProjectForHistory(project)) {
      events.add(ProjectTimelineEventResponseDto.builder()
          .type("CONTRACT")
          .title("[계약] " + nvl(c.getContractTitle()))
          .content("상태: " + c.getStatus().name())
          .owner(managerName)
          .occurredAt(c.getCreatedAt())
          .build());
    }

    // 매출(Revenue) – 프로젝트당 1건이라도 리스트로 처리
    for (Revenue r : revenueRepository.findByProjectForHistory(project)) {

      LocalDateTime when = r.getCreatedAt(); // 타임라인 기준: 생성 시간

      events.add(ProjectTimelineEventResponseDto.builder()
          .type("REVENUE")
          .title("[매출] 총 금액 " + r.getTotalPrice())
          .content("기준 임대료: " + r.getBaseRentSnapshot())
          .owner(managerName)
          .occurredAt(when)
          .build());
    }

    events.sort(Comparator.comparing(ProjectTimelineEventResponseDto::getOccurredAt));

    return ClientProjectHistoryItemResponseDto.builder()
        .projectId(project.getProjectId())
        .projectTitle(project.getTitle())
        .projectType(project.getType() != null ? project.getType().name() : null)
        .projectStatus(project.getStatus() != null ? project.getStatus().name() : null)
        .managerName(managerName)
        .startDay(project.getStartDay())
        .endDay(project.getEndDay())
        .events(events)
        .build();
  }

  private String nvl(String s) {
    return s == null ? "" : s;
  }
}
