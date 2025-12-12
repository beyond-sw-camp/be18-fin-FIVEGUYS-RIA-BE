package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreContractMapRepository extends JpaRepository<StoreContractMap, Long> {
    // 특정 계약에 매핑된 Tenant 조회
    List<StoreContractMap> findByContract(Contract contract);

    List<StoreContractMap> findByContract_ContractId(Long contractId);
}
