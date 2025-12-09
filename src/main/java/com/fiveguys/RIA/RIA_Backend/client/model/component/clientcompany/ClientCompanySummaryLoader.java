package com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySummaryResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreTenantMapRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientCompanySummaryLoader {

  private final ClientCompanyLoader clientCompanyLoader;
  private final ClientCompanyContractLoader clientCompanyContractLoader;
  private final StoreTenantMapRepository storeTenantMapRepository;

  public ClientCompanySummaryResponseDto load(Long clientCompanyId) {

    // 1) 고객사 기본 정보
    ClientCompany company = clientCompanyLoader.loadCompany(clientCompanyId);

    // 2) 최신 COMPLETED 계약
    Contract latestContract = clientCompanyContractLoader.loadLatestCompleted(company);

    // 3) 최신 입점 정보
    StoreTenantMap latestTenant = storeTenantMapRepository
        .findTopByClientCompanyOrderByStartDateDesc(company);

    String storeName = "-";
    Double areaSize = null;
    LocalDate moveInDate = null;

    if (latestTenant != null) {
      storeName = latestTenant.getStoreDisplayName();
      moveInDate = latestTenant.getStartDate();

      Store store = latestTenant.getStore();
      if (store != null) {
        areaSize = store.getAreaSize();
      }
    }

    LocalDate contractStart = null;
    LocalDate contractEnd = null;
    Long totalRent = null;
    BigDecimal commissionRate = null;

    if (latestContract != null) {
      contractStart = latestContract.getContractStartDate();
      contractEnd = latestContract.getContractEndDate();
      totalRent = latestContract.getTotalAmount();
      commissionRate = latestContract.getCommissionRate();
    }

    return ClientCompanySummaryResponseDto.builder()
        .clientCompanyId(company.getId())
        .companyName(company.getCompanyName())
        .storeDisplayName(storeName)
        .areaSize(areaSize)
        .moveInDate(moveInDate)
        .contractStartDate(contractStart)
        .contractEndDate(contractEnd)
        .totalRentAmount(totalRent)
        .commissionRate(commissionRate)
        .build();
  }
}
