package com.fiveguys.RIA.RIA_Backend.storemap.model.Service.impl;

import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Dto.StoreDetailStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Service.StoreDetailStatsService;
import com.fiveguys.RIA.RIA_Backend.storemap.model.Service.StoreMapService;
import com.fiveguys.RIA.RIA_Backend.storemap.model.component.StoreMapValidator;
import com.fiveguys.RIA.RIA_Backend.storemap.model.component.StoreStatsMapper;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreSalesStats;
import com.fiveguys.RIA.RIA_Backend.storemap.model.entity.StoreTenantMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreDetailStatsServiceImpl implements StoreDetailStatsService {

    private final StoreMapService service;
    private final StoreMapValidator validator;
    private final StoreStatsMapper mapper;

    @Override
    public StoreDetailStatsResponseDto getStoreDetail(Long storeId) {

        // 1) 매장 조회 (없으면 예외)
        Store store = validator.requireStore(service.findStore(storeId));

        // 2) 테넌트 조회 (없으면 null)
        StoreTenantMap tenant = validator.optionalTenant(
                service.findActiveTenant(storeId)
        );

        // 3) 테넌트가 있을 때에만 매출 조회
        StoreSalesStats stats = (tenant != null)
                ? validator.optionalStats(service.findStats(tenant.getId()))
                : null;

        // 4) 계약 정보 조회 (없으면 null)
        StoreContractMap contract = validator.optionalContract(
                service.findActiveContract(storeId)
        );

        // 5) DTO로 변환
        return mapper.toResponse(store, tenant, stats, contract);
    }


}
