package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RevenueMapper {
    public List<Revenue> toEntity(Contract contract, List<StoreContractMap> storeContracts, User user) {
        return storeContracts.stream()
                .map(storeContract -> Revenue.builder()
                        .project(contract.getProject())
                        .contract(contract)
                        .clientCompany(contract.getClientCompany())
                        .client(contract.getClient())
                        .pipeline(contract.getPipeline())
                        .createUser(user)
                        .baseRentSnapshot(storeContract.getRentPrice())      // StoreContractMap 기준
                        .commissionRateSnapshot(storeContract.getCommissionRate()) // StoreContractMap 기준
                        .totalPrice(BigDecimal.ZERO) // 초기 0
                        .status(Revenue.Status.ACTIVE)
                        .build())
                .collect(Collectors.toList());
    }
}
