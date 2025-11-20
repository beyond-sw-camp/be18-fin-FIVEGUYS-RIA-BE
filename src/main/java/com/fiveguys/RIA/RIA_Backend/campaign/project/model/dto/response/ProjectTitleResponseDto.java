package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectTitleResponseDto {
  private Long projectId;
  private String projectTitle;

/*  public static ProjectTitleResponseDto from(Project project) {
    return ProjectTitleResponseDto.builder()
        .projectId(project.getProjectId())
        .projectTitle(project.getTitle())
        .build();
  }*/
}
