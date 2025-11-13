package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
  boolean existsByTitleAndClientCompany(String title, ClientCompany clientCompany);
  boolean existsByProject(Project project);

}