package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    boolean existsByEstimateTitleAndClientCompany(String estimateTitle, ClientCompany clientCompany);

    // 견적서 조회
    @Query("""
        SELECT new com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateListResponseDto(
                e.estimateId,
                e.estimateTitle,
                e.clientCompany.companyName,
                e.client.name,
                e.createdUser.name,
                e.estimateDate,
                e.status
        )
        FROM Estimate e
        WHERE (:projectId IS NULL OR e.project.id = :projectId)
          AND (:clientCompanyId IS NULL OR e.clientCompany.id = :clientCompanyId)
          AND (:status IS NULL OR e.status = :status)
          AND (:keyword IS NULL OR e.estimateTitle LIKE CONCAT('%', :keyword, '%'))
          AND e.status <> com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate.Status.CANCELED
        ORDER BY e.createdAt DESC
        """)
    Page<EstimateListResponseDto> findEstimateList(
            @Param("projectId") Long projectId,
            @Param("clientCompanyId") Long clientCompanyId,
            @Param("keyword") String keyword,
            @Param("status") Estimate.Status status,
            Pageable pageable
    );

    // 상세 조회
    @Query("""
    SELECT e FROM Estimate e
    LEFT JOIN FETCH e.clientCompany
    LEFT JOIN FETCH e.client
    LEFT JOIN FETCH e.createdUser
    LEFT JOIN FETCH e.project
    LEFT JOIN FETCH e.pipeline
    WHERE e.estimateId = :id
""")
    Optional<Estimate> findDetailById(@Param("id") Long id);
}
