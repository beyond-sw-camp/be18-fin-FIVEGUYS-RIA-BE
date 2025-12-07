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
        long totalRentPrice = storeContracts.stream()
                .mapToLong(StoreContractMap::getRentPrice)
                .sum();

        long totalAdditional = storeContracts.stream()
                .mapToLong(StoreContractMap::getAdditionalFee)
                .sum();

        long totalDiscount = storeContracts.stream()
                .mapToLong(StoreContractMap::getDiscountAmount)
                .sum();

        long totalStoresAmount = totalRentPrice + totalAdditional - totalDiscount;

        // 보증금
        long contractAmount = contract.getContractAmount() != null ? contract.getContractAmount() : 0L;


        BigDecimal totalPrice;
        if (contract.getPaymentCondition() == Contract.PaymentCondition.PREPAY) {
            // PrePay
            totalPrice = BigDecimal.valueOf(totalStoresAmount + contractAmount);
        } else {
            // PostPay
            totalPrice = BigDecimal.ZERO; // 후불은 나중에 인식
        }

        return Revenue.builder()
                .project(contract.getProject())
                .contract(contract)
                .clientCompany(contract.getClientCompany())
                .client(contract.getClient())
                .pipeline(contract.getPipeline())
                .createUser(user)
                .baseRentSnapshot(totalRentPrice)
                .commissionRateSnapshot(contract.getCommissionRate())
                .totalPrice(totalPrice)
                .status(Revenue.Status.ACTIVE)
                .build();
    }
}
