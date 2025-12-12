package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectSearchRequestDto {

  private String status;
  private String keyword;
  private Boolean myProject;

  private String stages;

  public void setStatus(String status) { this.status = status; }
  public void setKeyword(String keyword) { this.keyword = keyword; }
  public void setMyProject(Boolean myProject) { this.myProject = myProject; }
  public void setStages(String stages) {this.stages = stages;}

  public List<String> getStageList() {
    if (stages == null || stages.isBlank()) return null;
    return Arrays.stream(stages.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .toList();
  }
}