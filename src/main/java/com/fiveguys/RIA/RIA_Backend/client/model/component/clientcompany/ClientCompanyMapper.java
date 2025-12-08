package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySimplePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySimpleResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.LeadCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.LeadCompanyListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ClientCompanyMapper {

  // 단일 엔티티 변환
  public ClientCompany toEntity(ClientCompanyRequestDto dto) {
    return ClientCompany.builder()
        .companyName(dto.getCompanyName())
        .category(dto.getCategory())
        .type(dto.getType())
        .businessNumber(dto.getBusinessNumber())
        .phone(dto.getPhone())
        .fax(dto.getFax())
        .website(dto.getWebsite())
        .zipCode(dto.getZipCode())
        .address(dto.getAddress())
        .build();
  }

  // 상세 DTO
  public ClientCompanyResponseDto toDetailDto(ClientCompany entity) {
    return ClientCompanyResponseDto.builder()
        .clientCompanyId(entity.getId())
        .companyName(entity.getCompanyName())
        .category(entity.getCategory())
        .type(entity.getType())
        .businessNumber(entity.getBusinessNumber())
        .phone(entity.getPhone())
        .fax(entity.getFax())
        .address(entity.getAddress())
        .website(entity.getWebsite())
        .zipCode(entity.getZipCode())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  // 고객사 목록 DTO (고객사 + 최신 COMPLETED 계약 정보 포함)
  public ClientCompanyListPageResponseDto toListPageDto(
      Page<ClientCompany> page,
      Map<Long, Contract> latestContractMap,
      int pageNumber,
      int size
  ) {
    List<ClientCompanyListResponseDto> data = page
        .getContent()
        .stream()
        .map(entity -> {
          Contract latest = (latestContractMap != null)
              ? latestContractMap.get(entity.getId())
              : null;
          return toListDto(entity, latest);
        })
        .collect(Collectors.toList());

    return ClientCompanyListPageResponseDto.builder()
        .totalCount(page.getTotalElements())
        .page(pageNumber)
        .size(size)
        .data(data)
        .build();
  }

  // 단일 고객사 + 최신 COMPLETED 계약 1건 → 리스트용 DTO
  public ClientCompanyListResponseDto toListDto(
      ClientCompany entity,
      Contract latest
  ) {
    String managerName = "-";
    String projectStatus = "-";
    LocalDate startDay = null;
    LocalDate endDay = null;

    if (latest != null) {
      startDay = latest.getContractStartDate();
      endDay = latest.getContractEndDate();

      Project project = latest.getProject();
      if (project != null) {
        if (project.getSalesManager() != null) {
          managerName = project.getSalesManager().getName();
        }
        if (project.getStatus() != null) {
          projectStatus = project.getStatus().name();
        }
      }
    }

    return ClientCompanyListResponseDto.builder()
        .clientCompanyId(entity.getId())
        .companyName(entity.getCompanyName())
        .category(entity.getCategory().name())
        .createdAt(entity.getCreatedAt())
        .managerName(managerName)
        .contractStartDay(startDay)
        .contractEndDay(endDay)
        .projectStatus(projectStatus)
        .build();
  }

  public LeadCompanyListPageResponseDto toLeadListPageDto(
      Page<ClientCompany> page,
      Map<Long, Client> mainContactMap,
      Map<Long, LocalDateTime> activityMap,
      int pageNumber,
      int size
  ) {
    List<LeadCompanyListResponseDto> data = page.getContent().stream()
        .map(company -> {
          Client main = mainContactMap.get(company.getId());
          LocalDateTime latestAt = activityMap.get(company.getId());
          return toLeadListDto(company, main, latestAt);
        })
        .toList();

    return LeadCompanyListPageResponseDto.builder()
        .totalCount(page.getTotalElements())
        .page(pageNumber)
        .size(size)
        .data(data)
        .build();
  }

  public LeadCompanyListResponseDto toLeadListDto(
      ClientCompany company,
      Client mainContact,
      LocalDateTime latestActivityAt
  ) {
    String contactName  = mainContact != null ? mainContact.getName()  : "-";
    String contactPhone = mainContact != null ? mainContact.getPhone() : "-";
    String contactEmail = mainContact != null ? mainContact.getEmail() : "-";

    // A안 + fallback:
    // 1순위: 활동들(Project/Proposal/Estimate/Contract/Revenue)의 최신 createdAt
    // 2순위: 담당자(Client) createdAt
    LocalDate latestDate = null;

    if (latestActivityAt != null) {
      latestDate = latestActivityAt.toLocalDate();
    } else if (mainContact != null && mainContact.getCreatedAt() != null) {
      latestDate = mainContact.getCreatedAt().toLocalDate();
    }

    return LeadCompanyListResponseDto.builder()
        .clientCompanyId(company.getId())
        .companyName(company.getCompanyName())
        .category(company.getCategory().name())
        .createdAt(company.getCreatedAt())
        .mainContactName(contactName)
        .mainContactPhone(contactPhone)
        .mainContactEmail(contactEmail)
        .latestActivityDate(latestDate)
        .build();
  }


  // 내부 조회용 (간단 목록)
  public ClientCompanySimpleResponseDto toSimpleDto(ClientCompany entity) {
    if (entity == null) return null;

    return ClientCompanySimpleResponseDto.builder()
        .id(entity.getId())
        .name(entity.getCompanyName())
        .build();
  }

  public ClientCompanySimplePageResponseDto toSimplePageDto(
      Page<ClientCompany> page,
      int currentPage,
      int pageSize
  ) {
    List<ClientCompanySimpleResponseDto> content = page.getContent().stream()
        .map(this::toSimpleDto)
        .toList();

    return ClientCompanySimplePageResponseDto.builder()
        .content(content)
        .totalCount(page.getTotalElements())
        .currentPage(currentPage)
        .pageSize(pageSize)
        .build();
  }
}
