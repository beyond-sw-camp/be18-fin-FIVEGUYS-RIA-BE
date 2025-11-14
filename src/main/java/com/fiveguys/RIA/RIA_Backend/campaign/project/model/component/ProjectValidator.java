package com.fiveguys.RIA.RIA_Backend.campaign.project.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request.ProjectUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProjectErrorCode;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {

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
}