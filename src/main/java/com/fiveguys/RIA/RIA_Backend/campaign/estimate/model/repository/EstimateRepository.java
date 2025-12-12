package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response.EstimateListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.projection.CompanyActivityDateProjection;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
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
      AND (:excludeStatus IS NULL OR e.status <> :excludeStatus)
    ORDER BY e.createdAt DESC
""")
    Page<EstimateListResponseDto> findEstimateList(
            @Param("projectId") Long projectId,
            @Param("clientCompanyId") Long clientCompanyId,
            @Param("keyword") String keyword,
            @Param("status") Estimate.Status status,
            @Param("excludeStatus") Estimate.Status excludeStatus,
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


    // 업데이트 시 제목 중복
    @Query("""
    SELECT COUNT(e) > 0
    FROM Estimate e
    WHERE e.estimateTitle = :title
    AND e.clientCompany = :company
    AND e.estimateId <> :estimateId""")
    boolean existsDuplicateTitle(String title, ClientCompany company, Long estimateId);

    List<Estimate> findByProject(Project project);

    @Query("""
    select e
    from Estimate e
    where e.project = :project
    order by e.createdAt asc
    """)
    List<Estimate> findByProjectForHistory(@Param("project") Project project);

    @Query("""
    select e
    from Estimate e
    where e.project.client.id = :clientId
    order by e.createdAt desc
    """)
    List<Estimate> findHistoryEstimatesByClient(@Param("clientId") Long clientId);
}
