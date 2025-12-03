package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    boolean existsByContractTitleAndClientCompany(String contractTitle, ClientCompany company);

    @Query("""
        SELECT e
        FROM Estimate e
        JOIN e.clientCompany cc
        JOIN e.client c
        WHERE e.status = :status
          AND (:projectId IS NULL OR e.project.projectId = :projectId)
          AND (:clientCompanyId IS NULL OR cc.id = :clientCompanyId)
          AND (:keyword IS NULL OR LOWER(e.estimateTitle) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:userId IS NULL OR e.createdUser.id = :userId)
        ORDER BY e.createdAt DESC
    """)
    List<Estimate> findContractEstimateList(
            @Param("projectId") Long projectId,
            @Param("clientCompanyId") Long clientCompanyId,
            @Param("keyword") String keyword,
            @Param("status") Estimate.Status status,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
    SELECT new com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractListResponseDto(
        c.contractId,
        c.contractTitle,
        cc.id,
        cc.companyName,
        cl.name,
        p.projectId,
        p.title,
        c.contractStartDate,
        c.contractEndDate,
        c.contractDate,
        c.totalAmount,
        c.status,
        c.createdUser.id,
        c.createdAt
    )
    FROM Contract c
    JOIN c.project p
    JOIN c.clientCompany cc
    JOIN c.client cl
    WHERE (:projectId IS NULL OR p.projectId = :projectId)
      AND (:clientCompanyId IS NULL OR cc.id = :clientCompanyId)
      AND (:keyword IS NULL OR c.contractTitle LIKE %:keyword%)
      AND (:status IS NULL OR c.status = :status)
      AND (:contractDate IS NULL OR DATE(c.contractDate) = DATE(:contractDate))
    ORDER BY c.createdAt DESC
""")
    Page<ContractListResponseDto> findContractList(
            @Param("projectId") Long projectId,
            @Param("clientCompanyId") Long clientCompanyId,
            @Param("keyword") String keyword,
            @Param("status") Contract.Status status,
            @Param("contractDate") LocalDate contractDate,
            Pageable pageable
    );
}
