package com.fiveguys.RIA.RIA_Backend.user.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.MyProjectResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.ProfileResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.response.UserSimpleResponseDto;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
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
    double marginRate = p.getExpectedMarginRate() != null
        ? p.getExpectedMarginRate().doubleValue()
        : 0.0;

    int expectedRevenue = p.getExpectedRevenue() != null
        ? p.getExpectedRevenue()
        : 0;

    int expectedProfit = (int) (expectedRevenue * (marginRate / 100));

    return MyProjectResponseDto.builder()
        .projectId(p.getProjectId())
        .title(p.getTitle())
        .clientCompanyName(p.getClientCompany().getCompanyName())
        .startDay(p.getStartDay())
        .endDay(p.getEndDay())
        .expectedRevenue(expectedRevenue)
        .expectedMarginRate(marginRate)
        .expectedProfit(expectedProfit)
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
}