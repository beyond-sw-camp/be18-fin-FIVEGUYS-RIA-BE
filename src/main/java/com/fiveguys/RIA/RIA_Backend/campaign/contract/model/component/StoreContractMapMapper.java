package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.UpdateContractSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailSpaceResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreContractMapMapper {

    public StoreContractMap toEntity(
            CreateContractSpaceRequestDto spaceDto,
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
                .rentPrice(fixRentPrice(contract.getContractType(), store.getRentPrice()))
                .additionalFee(additionalFee)
                .discountAmount(discountAmount)
                .finalContractAmount(finalContractAmount)
                .description(spaceDto.getDescription() != null ? spaceDto.getDescription() : "")
                .build();
    }

    // 오버로딩
    public StoreContractMap toEntity(
            UpdateContractSpaceRequestDto spaceDto,
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
                .rentPrice(fixRentPrice(contract.getContractType(), store.getRentPrice()))
                .additionalFee(additionalFee)
                .discountAmount(discountAmount)
                .finalContractAmount(finalContractAmount)
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
                .description(map.getDescription())
                .build();
    }

    private Long fixRentPrice(Contract.ContractType type, Long rentPrice) {
        if (type == Contract.ContractType.CONSIGNMENT) return 0L;
        return rentPrice; // LEASE/MIX → 그대로
    }
}
