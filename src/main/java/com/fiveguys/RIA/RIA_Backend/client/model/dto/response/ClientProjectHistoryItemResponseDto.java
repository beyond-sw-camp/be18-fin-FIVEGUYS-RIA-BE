package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientProjectHistoryItemResponseDto {

  private Long projectId;
  private String projectTitle;
  private String projectType;    // POPUP / EXHIBITION / RENTAL
  private String projectStatus;  // ACTIVE / COMPLETED / CANCELLED

  private String managerName;    // Project.salesManager.name

  private LocalDate startDay;
  private LocalDate endDay;

  // 이 프로젝트 밑의 타임라인 이벤트들 (시간순 정렬)
  private List<ProjectTimelineEventResponseDto> events;
}
