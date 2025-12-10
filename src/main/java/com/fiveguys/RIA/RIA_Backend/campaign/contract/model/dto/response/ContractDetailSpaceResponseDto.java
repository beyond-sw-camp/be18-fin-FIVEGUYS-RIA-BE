package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractDetailSpaceResponseDto {
    // 계약 상세 조회

    private final Long storeContractMapId;

    private final Long storeId;

    private final Long floorId;

    private final String floorName;

    private final String storeNumber;

    private final Double areaSize;

    private final Long rentPrice;

    private final Long additionalFee;

    private final Long discountAmount;

    private final Store.StoreType storeType;

    private final Long finalContractAmount;

    private final String description;
}
