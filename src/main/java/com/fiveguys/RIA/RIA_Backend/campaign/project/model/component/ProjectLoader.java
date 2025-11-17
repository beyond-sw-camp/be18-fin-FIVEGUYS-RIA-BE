package com.fiveguys.RIA.RIA_Backend.campaign.project.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.repository.PipelineRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectLoader {

  private final ProjectRepository projectRepository;
  private final PipelineRepository pipelineRepository;
  private final ClientCompanyRepository clientCompanyRepository;
  private final ClientRepository clientRepository;
  private final UserRepository userRepository;

  // salesManager 포함 프로젝트 조회
  public Project loadProjectWithSalesManager(Long id) {
    return projectRepository.findByIdWithSalesManager(id)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.PROJECT_NOT_FOUND));
  }

  // 고객사
  public ClientCompany loadClientCompany(Long id) {
    return clientCompanyRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.CLIENT_COMPANY_NOT_FOUND));
  }

  // 고객
  public Client loadClient(Long id) {
    return clientRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.CLIENT_NOT_FOUND));
  }

  // 담당자(영업 매니저)
  public User loadUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.SALES_MANAGER_NOT_FOUND));
  }


  public Project loadDetail(Long projectId) {
    return projectRepository.findByProjectId(projectId)
        .orElseThrow(() -> new CustomException(ProjectErrorCode.PROJECT_NOT_FOUND));
  }
}