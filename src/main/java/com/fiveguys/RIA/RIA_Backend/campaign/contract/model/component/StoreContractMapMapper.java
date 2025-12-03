package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.ContractSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailSpaceResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.StoreEstimateMap;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ContractErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StoreContractMapMapper {

    public StoreContractMap toEntity(
            ContractSpaceRequestDto spaceDto,
            Contract contract,
            Store store
    ) {
        // 추가비용과 할인금액 기본값 처리
        long additionalFee = spaceDto.getAdditionalFee() != null ? spaceDto.getAdditionalFee() : 0L;
        long discountAmount = spaceDto.getDiscountAmount() != null ? spaceDto.getDiscountAmount() : 0L;

        // 최종 계약 금액 계산 (기본 임대료 + 추가비용 - 할인금액)
        long finalContractAmount = store.getRentPrice() + additionalFee - discountAmount;

        return StoreContractMap.builder()
                .store(store)
                .contract(contract)
                .areaSize(store.getAreaSize())
                .rentPrice(store.getRentPrice())
                .additionalFee(additionalFee)
                .discountAmount(discountAmount)
                .finalContractAmount(finalContractAmount)
                .contractStartDate(spaceDto.getContractStartDate())
                .contractEndDate(spaceDto.getContractEndDate())
                .commissionRate(spaceDto.getCommissionRate())
                .description(spaceDto.getDescription() != null ? spaceDto.getDescription() : "")
                .build();
    }

    public ContractDetailSpaceResponseDto toResponseDto(StoreContractMap map) {

        return ContractDetailSpaceResponseDto.builder()
                .storeContractMapId(map.getStoreContractMapId())
                .storeId(map.getStore().getStoreId())
                .floorId(map.getStore().getFloor().getFloorId())
                .storeNumber(map.getStore().getStoreNumber())
                .finalContractAmount(map.getFinalContractAmount())
                .contractStartDate(map.getContractStartDate())
                .contractEndDate(map.getContractEndDate())
                .commissionRate(map.getCommissionRate())
                .description(map.getDescription())
                .build();
    }
}
