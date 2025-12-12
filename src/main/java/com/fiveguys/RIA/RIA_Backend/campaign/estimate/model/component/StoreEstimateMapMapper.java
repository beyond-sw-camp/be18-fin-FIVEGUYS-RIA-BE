package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.request.EstimateSpaceRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.StoreEstimateMap;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreEstimateMapMapper {

    public StoreEstimateMap toEntity(Store store, Estimate estimate, EstimateSpaceRequestDto spaceDto) {

        long add = spaceDto.getAdditionalFee() != null ? spaceDto.getAdditionalFee() : 0L;
        long discount = spaceDto.getDiscountAmount() != null ? spaceDto.getDiscountAmount() : 0L;

        long finalAmount = store.getRentPrice() + add - discount;

        return StoreEstimateMap.builder()
                .store(store)
                .estimate(estimate)
                .areaSize(store.getAreaSize())
                .rentPrice(store.getRentPrice())
                .additionalFee(add)
                .discountAmount(discount)
                .finalEstimateAmount(finalAmount)
                .description(spaceDto.getDescription())
                .build();
    }
}
