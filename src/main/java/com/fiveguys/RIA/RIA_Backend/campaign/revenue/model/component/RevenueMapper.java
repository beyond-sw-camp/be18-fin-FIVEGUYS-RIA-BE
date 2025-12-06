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
//                .commissionRateSnapshot(contract.getCommissionRate())
            .totalPrice(BigDecimal.ZERO)          // 초기 0
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
            //.contractDate(p.getContractDate())
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

            .latestSettlementYear(latest.getSettlementYear())
            .latestSettlementMonth(latest.getSettlementMonth())
            .latestTotalSalesAmount(latest.getTotalSalesAmount())
            .latestCommissionRate(latest.getCommissionRate())
            .latestCommissionAmount(latest.getCommissionAmount())
            .latestFinalRevenue(latest.getFinalRevenue())
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