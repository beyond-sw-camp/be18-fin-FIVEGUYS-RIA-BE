package com.fiveguys.RIA.RIA_Backend.campaign.project.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectValidator {

  private final ProjectRepository projectRepository;
  private final ContractRepository contractRepository;

  //생성시 필수값 검증
  public void validateCreate(ProjectCreateRequestDto dto) {

    if (dto.getClientCompanyId() == null) {
      throw new CustomException(ProjectErrorCode.CLIENT_COMPANY_NOT_FOUND);
    }

    if (dto.getClientId() == null) {
      throw new CustomException(ProjectErrorCode.CLIENT_NOT_FOUND);
    }

    if (dto.getTitle() == null || dto.getTitle().isBlank()) {
      throw new CustomException(ProjectErrorCode.TITLE_REQUIRED);
    }

    if (dto.getType() == null || dto.getType().isBlank()) {
      throw new CustomException(ProjectErrorCode.TYPE_REQUIRED);
    }

    // 날짜 필수
    if (dto.getStartDay() == null || dto.getEndDay() == null) {
      throw new CustomException(ProjectErrorCode.INVALID_DATE_RANGE);
    }

    // 날짜 역전
    if (dto.getStartDay().isAfter(dto.getEndDay())) {
      throw new CustomException(ProjectErrorCode.INVALID_DATE_RANGE);
    }
  }

  //수정시 입력 검증
  public void validateUpdate(ProjectUpdateRequestDto dto) {

    // 제목이 들어왔는데 blank면 안 됨
    if (dto.getTitle() != null && dto.getTitle().isBlank()) {
      throw new CustomException(ProjectErrorCode.TITLE_REQUIRED);
    }

    // 날짜 둘 다 들어왔으면 검증
    if (dto.getStartDay() != null && dto.getEndDay() != null) {
      if (dto.getStartDay().isAfter(dto.getEndDay())) {
        throw new CustomException(ProjectErrorCode.INVALID_DATE_RANGE);
      }
    }
  }

  public Project.Status parseStatus(String status) {
    if (status == null || status.isBlank()) {
      return null; // 필터 안 쓴 것
    }

    try {
      return Project.Status.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new CustomException(ProjectErrorCode.INVALID_STATUS);
    }
  }

  public void validateCancelable(Project p) {
    if (p.getStatus() == Project.Status.COMPLETED) {
      throw new CustomException(ProjectErrorCode.CANNOT_CANCEL_COMPLETED_PROJECT);
    }
  }

  public void validateDuplicate(String title, ClientCompany company) {
    if (projectRepository.existsByTitleAndClientCompany(title, company)) {
      throw new CustomException(ProjectErrorCode.DUPLICATE_PROJECT);
    }
  }

  public void validateManagerChange(Project project, Long newManagerId) {

    if (newManagerId == null) {
      throw new CustomException(ProjectErrorCode.SALES_MANAGER_NOT_FOUND);
    }

    if (project.getSalesManager() != null
        && project.getSalesManager().getId().equals(newManagerId)) {
      throw new CustomException(ProjectErrorCode.MANAGER_NOT_CHANGED);
    }

    boolean hasCompletedContract =
        contractRepository.existsByProjectAndStatus(project, Contract.Status.COMPLETED);

    if (hasCompletedContract) {
      throw new CustomException(ProjectErrorCode.CANNOT_CHANGE_MANAGER_AFTER_COMPLETED_CONTRACT);
    }
  }
  public void validateManagerChangePermission(User actor) {
    if (actor == null || actor.getRole() == null || actor.getRole().getRoleName() == null) {
      throw new CustomException(ProjectErrorCode.PERMISSION_DENIED);
    }

    String roleName = actor.getRole().getRoleName().name(); // ROLE_ADMIN, ROLE_SALES_LEAD, ROLE_SALES_MEMBER ...

    if (!"ROLE_ADMIN".equals(roleName) && !"ROLE_SALES_LEAD".equals(roleName)) {
      throw new CustomException(ProjectErrorCode.PERMISSION_DENIED);
    }
  }
}