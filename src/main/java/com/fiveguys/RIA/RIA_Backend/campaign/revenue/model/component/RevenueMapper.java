package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueListItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueSettlementHistoryItemResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response.RevenueSettlementHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.RevenueSettlement;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.LatestSettlementProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueDetailProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.RevenueListProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.SettlementAggProjection;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.projection.StoreInfoProjection;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class RevenueMapper {

    public Revenue toEntity(Contract contract, List<StoreContractMap> storeContracts, User user) {
        long totalRentPrice = storeContracts.stream()
                .mapToLong(StoreContractMap::getRentPrice)
                .sum();

        // 모든 매장의 rentPrice 합산
        long totalBaseRent = storeContracts.stream()
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
                .totalPrice(totalPrice)
                .status(Revenue.Status.ACTIVE)
                .build();
    }

    public RevenueListItemResponseDto toRevenueListItemResponseDto(RevenueListProjection p) {
        return RevenueListItemResponseDto.builder()
            .revenueId(p.getRevenueId())
            .projectId(p.getProjectId())
            .contractId(p.getContractId())
            .settlementId(p.getSettlementId())

            .contractTitle(p.getContractTitle())
            .clientCompanyName(p.getClientCompanyName())

            .settlementYear(p.getSettlementYear())
            .settlementMonth(p.getSettlementMonth())
            .finalRevenue(p.getFinalRevenue())

            .storeType(p.getStoreType())

            .managerId(p.getManagerId())
            .managerName(p.getManagerName())

            .contractStartDay(p.getContractStartDay())
            .contractEndDay(p.getContractEndDay())
            .build();
    }

    // ===== 상세 매핑 =====
    public RevenueDetailResponseDto toRevenueDetailResponseDto(
            RevenueDetailProjection p,
            List<StoreInfoProjection> stores,
            SettlementAggProjection agg,
            LatestSettlementProjection latest
    ) {


        Integer latestYear = latest != null ? latest.getSettlementYear() : null;
        Integer latestMonth = latest != null ? latest.getSettlementMonth() : null;

        BigDecimal latestTotalSalesAmount =
                latest != null ? latest.getTotalSalesAmount() : null;

        BigDecimal latestCommissionRate =
                latest != null ? latest.getCommissionRate() : null;

        BigDecimal latestCommissionAmount =
                latest != null ? latest.getCommissionAmount() : null;

        BigDecimal latestFinalRevenue =
                latest != null ? latest.getFinalRevenue() : null;

        return RevenueDetailResponseDto.builder()
                .revenueId(p.getRevenueId())
                .projectId(p.getProjectId())
                .projectTitle(p.getProjectTitle())
                .projectType(p.getProjectType())
                .salesManagerName(p.getSalesManagerName())

                .contractId(p.getContractId())
                .contractTitle(p.getContractTitle())
                .contractType(p.getContractType())
                .contractStartDate(p.getContractStartDate())
                .contractEndDate(p.getContractEndDate())
                .commissionRate(p.getCommissionRate())
                .paymentCondition(p.getPaymentCondition())
                .depositAmount(p.getDepositAmount())
                .currency(p.getCurrency())

                .clientCompanyName(p.getClientCompanyName())
                .clientName(p.getClientName())
                .baseRentSnapshot(p.getBaseRentSnapshot())

                .stores(
                        stores.stream()
                                .map(s -> RevenueDetailResponseDto.StoreInfo.builder()
                                        .storeTenantMapId(s.getStoreTenantMapId())
                                        .floorName(s.getFloorName())
                                        .storeNumber(s.getStoreNumber())
                                        .storeDisplayName(s.getStoreDisplayName())
                                        .finalContractAmount(s.getFinalContractAmount())
                                        .build()
                                ).toList()
                )

                .totalSalesAccumulated(agg.getTotalSalesAccumulated())
                .commissionAmountAccumulated(agg.getCommissionAmountAccumulated())
                .finalRevenueAccumulated(agg.getFinalRevenueAccumulated())

                // 최신 정산(BigDecimal + null-safe)
                .latestSettlementYear(latestYear)
                .latestSettlementMonth(latestMonth)
                .latestTotalSalesAmount(latestTotalSalesAmount)
                .latestCommissionRate(latestCommissionRate)
                .latestCommissionAmount(latestCommissionAmount)
                .latestFinalRevenue(latestFinalRevenue)

                .build();
    }
    // ===== 정산 히스토리 DTO 매핑 =====

    public RevenueSettlementHistoryResponseDto toRevenueSettlementHistoryResponseDto(
        Long contractId,
        int startYear,
        int startMonth,
        int endYear,
        int endMonth,
        List<RevenueSettlement> settlements
    ) {
        List<RevenueSettlementHistoryItemResponseDto> items = settlements.stream()
            .map(this::toRevenueSettlementHistoryItemResponseDto)
            .collect(Collectors.toList());

        return RevenueSettlementHistoryResponseDto.builder()
            .contractId(contractId)
            .startYear(startYear)
            .startMonth(startMonth)
            .endYear(endYear)
            .endMonth(endMonth)
            .settlements(items)
            .build();
    }

    public RevenueSettlementHistoryItemResponseDto toRevenueSettlementHistoryItemResponseDto(
        RevenueSettlement rs
    ) {
        return RevenueSettlementHistoryItemResponseDto.builder()
            .settlementYear(rs.getSettlementYear())
            .settlementMonth(rs.getSettlementMonth())
            .totalSalesAmount(rs.getTotalSalesAmount())
            .commissionRate(rs.getCommissionRate())
            .commissionAmount(rs.getCommissionAmount())
            .finalRevenue(rs.getFinalRevenue())
            .build();
    }
}