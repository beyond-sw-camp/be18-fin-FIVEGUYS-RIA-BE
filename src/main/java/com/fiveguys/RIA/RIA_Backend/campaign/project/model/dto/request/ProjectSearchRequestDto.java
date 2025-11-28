package com.fiveguys.RIA.RIA_Backend.campaign.project.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectSearchRequestDto {

  private String status;
  private String keyword;
  private Boolean myProject;

  //필터를 걸때마다 내부적으로 날리는 쿼리에 바인딩을 하려면 어쩔수없이 세터를 쓸수밖에 없었음 세터 안쓸꺼면 specificaion utill 만들어서
  //수동으로 join 해주는 문법 만들어야하는데 그렇게 하긴... 대신 해주실분 구함
  public void setStatus(String status) { this.status = status; }
  public void setKeyword(String keyword) { this.keyword = keyword; }
  public void setMyProject(Boolean myProject) { this.myProject = myProject; }
}