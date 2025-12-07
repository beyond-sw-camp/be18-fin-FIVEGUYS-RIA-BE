package com.fiveguys.RIA.RIA_Backend.facility.store.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreTenantMapMapper {

    public List<StoreTenantMap> toEntity(Contract contract, List<Store> stores) {
        return stores.stream()
                .map(store -> StoreTenantMap.builder()
                        .store(store)
                        .contract(contract)
                        .clientCompany(contract.getClientCompany())
                        .client(contract.getClient())
                        .storeDisplayName(store.getStoreNumber())
                        .startDate(contract.getContractStartDate())
                        .endDate(contract.getContractEndDate())
                        .status(StoreTenantMap.Status.ACTIVE)
                        .build()
                )
                .collect(Collectors.toList());
    }

}
