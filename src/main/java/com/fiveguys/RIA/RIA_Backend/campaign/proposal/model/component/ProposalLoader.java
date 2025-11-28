package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository.ProposalRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProposalErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProposalLoader {

  private final ProposalRepository proposalRepository;
  private final ProjectRepository projectRepository;
  private final ClientCompanyRepository clientCompanyRepository;
  private final ClientRepository clientRepository;
  private final UserRepository userRepository;

  //유저 로딩
  public User loadUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProposalErrorCode.USER_NOT_FOUND));
  }

  // 제안서 로딩
  public Proposal loadProposal(Long id) {
    return proposalRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProposalErrorCode.PROPOSAL_NOT_FOUND));
  }

  // 프로젝트 로딩
  public Project loadProject(Long id) {
    if (id == null) return null;
    return projectRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProposalErrorCode.PROJECT_NOT_FOUND));
  }
  // 고객사 로딩
  public ClientCompany loadCompany(Long id) {
    if (id == null) return null;
    return clientCompanyRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProposalErrorCode.CLIENT_COMPANY_NOT_FOUND));
  }
  // 고객사 담당자 로딩
  public Client loadClient(Long id) {
    if (id == null) return null;
    return clientRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProposalErrorCode.CLIENT_NOT_FOUND));
  }

  //프로젝트 파이프라인 로딩
  public Project loadProjectWithPipeline(Long projectId) {
    return projectRepository.findByIdWithPipeline(projectId)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.PROJECT_NOT_FOUND));
  }

  //제안서 상세 조회(연관관계 포함)
  public Proposal loadProposalDetail(Long id) {
    return proposalRepository.findDetailById(id)
        .orElseThrow(() -> new CustomException(ProposalErrorCode.PROPOSAL_NOT_FOUND));
  }

  public List<Proposal> loadByProjectId(Long projectId) {
    return proposalRepository.findByProject_ProjectId(projectId);
  }
}