package com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  // 프로젝트명 + 고객사 기준 중복 방지 체크
  boolean existsByTitleAndClientCompany(String title, ClientCompany clientCompany);

  @Query("""
      SELECT p
      FROM Project p
      JOIN FETCH p.clientCompany cc
      JOIN FETCH p.salesManager sm
      WHERE sm.id = :userId
        AND p.status = 'ACTIVE'
      ORDER BY p.createdAt DESC
      """)
  List<Project> findActiveProjectsByUserId(
      @Param("userId") Long userId,
      Pageable pageable
  );

  @Query("""
    SELECT DISTINCT p
    FROM Project p
    JOIN FETCH p.clientCompany cc
    JOIN FETCH p.salesManager sm
    LEFT JOIN FETCH p.pipeline pl
    WHERE (:status IS NULL OR p.status = :status)
      AND (:keyword IS NULL OR p.title LIKE %:keyword% OR cc.companyName LIKE %:keyword%)
      AND (:managerName IS NULL OR sm.name LIKE %:managerName%)
      AND p.status <> 'CANCELLED'
    ORDER BY p.createdAt DESC
""")
  List<Project> findProjectsWithFilters(
      @Param("status") Project.Status status,
      @Param("keyword") String keyword,
      @Param("managerName") String managerName,
      Pageable pageable
  );

  @EntityGraph(attributePaths = {
      "clientCompany",
      "client",
      "salesManager",
      "pipeline",
      "proposals"  // 연관된 제안까지 페치
      //estimate , contract , revenue 추후 연결
  })
  Optional<Project> findByProjectId(Long projectId);

  @Query("""
        select p from Project p
        join fetch p.salesManager sm
        where p.projectId = :projectId
    """)
  Optional<Project> findByIdWithSalesManager(@Param("projectId") Long projectId);

  @Query("""
    SELECT p 
    FROM Project p
    LEFT JOIN FETCH p.pipeline 
    WHERE p.id = :projectId
    """)
  Optional<Project> findByIdWithPipeline(@Param("projectId") Long projectId);
}