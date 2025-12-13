package com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.projection.CompanyActivityDateProjection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
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

  @Query(
      value = """
      SELECT DISTINCT p
      FROM Project p
      JOIN FETCH p.clientCompany cc
      JOIN FETCH p.salesManager sm
      LEFT JOIN FETCH p.pipeline pl
      WHERE (:status IS NULL OR p.status = :status)
        AND (
          :keyword IS NULL
          OR p.title LIKE CONCAT('%', :keyword, '%')
          OR cc.companyName LIKE CONCAT('%', :keyword, '%')
        )
        AND (:managerId IS NULL OR sm.id = :managerId)
        AND (:stages IS NULL OR pl.currentStage IN :stages)
        AND (
          :status = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project.Status.CANCELLED
          OR p.status <> com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project.Status.CANCELLED
        )
      """,
      countQuery = """
      SELECT COUNT(DISTINCT p)
      FROM Project p
      JOIN p.clientCompany cc
      JOIN p.salesManager sm
      JOIN p.pipeline pl
      WHERE (:status IS NULL OR p.status = :status)
        AND (
          :keyword IS NULL
          OR p.title LIKE CONCAT('%', :keyword, '%')
          OR cc.companyName LIKE CONCAT('%', :keyword, '%')
        )
        AND (:managerId IS NULL OR sm.id = :managerId)
        AND (:stages IS NULL OR pl.currentStage IN :stages)
        AND (
          :status = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project.Status.CANCELLED
          OR p.status <> com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project.Status.CANCELLED
        )
      """
  )
  Page<Project> findProjectsWithFilters(
      @Param("status") Project.Status status,
      @Param("keyword") String keyword,
      @Param("managerId") Long managerId,
      @Param("stages") List<String> stages,
      Pageable pageable
  );



  @EntityGraph(attributePaths = {
      "clientCompany",
      "client",
      "salesManager",
      "pipeline",
      "proposals",
      "revenue"
      //estimate , contract  추후 연결
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

  @Query("""
    select p
    from Project p
    where (:keyword is null or p.title like concat('%', :keyword, '%'))
      and p.status <> com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project.Status.CANCELLED
    order by p.createdAt desc
    """)
  List<Project> findTitleOptions(@Param("keyword") String keyword);

  @Query("""
      select p
      from Project p
      where p.salesManager.id = :managerId
        and p.type = com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project$Type.RENTAL
        and p.startDay <= :monthEnd
        and p.endDay   >= :monthStart
      """)
  List<Project> findRentalProjectsActiveInMonth(
      @Param("managerId") Long managerId,
      @Param("monthStart") LocalDate monthStart,
      @Param("monthEnd") LocalDate monthEnd
  );

  @Query("""
    select p
    from Project p
    join fetch p.client c
    left join fetch p.salesManager m
    where c.id = :clientId
    order by p.createdAt desc
    """)
  List<Project> findHistoryProjectsByClient(@Param("clientId") Long clientId);

  @Query("""
    SELECT p
    FROM Project p
    WHERE p.salesManager.id = :userId
      AND p.status = 'ACTIVE'
      AND (:keyword IS NULL OR p.title LIKE %:keyword%)
""")
  List<Project> findByUserIdAndTitleLike(
          @Param("userId") Long userId,
          @Param("keyword") String keyword
  );


/*  @Query("""
      select 
        p.clientCompany.id as clientCompanyId,
        max(p.createdAt)   as latestAt
      from Project p
      where p.clientCompany.id in :companyIds
      group by p.clientCompany.id
      """)
  List<CompanyActivityDateProjection> findLatestProjectActivityByClientCompanyIds(
      @Param("companyIds") List<Long> companyIds
  );*/

}
