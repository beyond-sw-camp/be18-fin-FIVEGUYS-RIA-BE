package com.fiveguys.RIA.RIA_Backend.user.model.component;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.UserResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.MyProjectResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.ProfileResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.UserSimpleResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public ProfileResponseDto toProfileDto(User user) {
    return ProfileResponseDto.builder()
        .userId(user.getId())
        .department(user.getDepartment().name())
        .email(user.getEmail())
        .employeeId(user.getEmployeeNo())
        .name(user.getName())
        .position(user.getPosition())
        .build();
  }

  public MyProjectResponseDto toMyProject(Project p) {

/*
    화면 설계서상 나의 프로젝트 조회에서 금액 관련 내용이 없어서 주석 처리함
    BigDecimal revenue = p.getExpectedRevenue() != null
        ? p.getExpectedRevenue()
        : BigDecimal.ZERO;

    BigDecimal marginRate = p.getExpectedMarginRate() != null
        ? p.getExpectedMarginRate()
        : BigDecimal.ZERO;

    BigDecimal expectedProfitBd = revenue
        .multiply(marginRate)
        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

    Integer expectedProfit = expectedProfitBd.intValue();
*/

    return MyProjectResponseDto.builder()
        .projectId(p.getProjectId())
        .title(p.getTitle())
        .clientCompanyName(p.getClientCompany().getCompanyName())
        .startDay(p.getStartDay())
        .endDay(p.getEndDay())
        .type(p.getType().name())
        .salesManagerName(p.getSalesManager().getName())
        .status(p.getStatus().name())
        .build();
  }

  public UserSimpleResponseDto toSimpleDto(User user) {
    return UserSimpleResponseDto.builder()
        .userId(user.getId())
        .name(user.getName())
        .build();
  }

  public UserResponseDto toDto(User user) {
    if (user == null) return null;

    return UserResponseDto.builder()
                          .id(user.getId())
                          .employeeNo(user.getEmployeeNo())
                          .name(user.getName())
                          .email(user.getEmail())
                          .department(
                                  user.getDepartment() != null
                                          ? user.getDepartment().name()
                                          : null
                          )
                          .position(user.getPosition())
                          .state(
                                  user.getStatus() != null
                                          ? user.getStatus().name()
                                          : null
                          )
                          .roleId(user.getRole() != null ? user.getRole().getId() : null)
                          .build();
  }
}
