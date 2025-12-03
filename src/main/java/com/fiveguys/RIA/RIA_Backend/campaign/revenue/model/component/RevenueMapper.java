package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class RevenueMapper {

    public Revenue toEntity(Contract contract, List<StoreContractMap> storeContracts, User user) {
        // 모든 매장의 rentPrice 합산
        long totalBaseRent = storeContracts.stream()
                .mapToLong(StoreContractMap::getRentPrice)
                .sum();

        return Revenue.builder()
                .project(contract.getProject())
                .contract(contract)
                .clientCompany(contract.getClientCompany())
                .client(contract.getClient())
                .pipeline(contract.getPipeline())
                .createUser(user)
                .baseRentSnapshot(totalBaseRent)       // 합산된 임대료
                .commissionRateSnapshot(contract.getCommissionRate())
                .totalPrice(BigDecimal.ZERO)          // 초기 0
                .status(Revenue.Status.ACTIVE)
                .build();
    }
}
