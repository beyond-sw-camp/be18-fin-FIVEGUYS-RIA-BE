package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
  boolean existsByTitleAndClientCompany(String title, ClientCompany clientCompany);

  Optional<Proposal> findById(Long id);

  @Query("""
        SELECT new com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response.ProposalListResponseDto(
                p.proposalId,
                p.title,
                p.clientCompany.companyName,
                p.client.name,
                p.createdUser.name,
                p.requestDate,
                p.submitDate,
                p.status
        )
        FROM Proposal p
        WHERE p.status != com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal$Status.CANCELED
          AND (:projectId IS NULL OR p.project.projectId = :projectId)
          AND (:clientCompanyId IS NULL OR p.clientCompany.id = :clientCompanyId)
          AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%'))
          AND (:status IS NULL OR p.status = :status)
        ORDER BY p.createdAt DESC
    """)
  Page<ProposalListResponseDto> findProposalList(
      Long projectId,
      Long clientCompanyId,
      String keyword,
      Proposal.Status status,
      Pageable pageable
  );
  @Query("""
    SELECT p FROM Proposal p
        LEFT JOIN FETCH p.project
        LEFT JOIN FETCH p.client
        LEFT JOIN FETCH p.clientCompany
        LEFT JOIN FETCH p.createdUser
    WHERE p.proposalId = :id
""")
  Optional<Proposal> findDetailById(@Param("id") Long id);

}