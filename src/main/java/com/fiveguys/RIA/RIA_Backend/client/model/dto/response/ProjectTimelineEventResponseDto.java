package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectTimelineEventResponseDto {

  // PROJECT / PROPOSAL / ESTIMATE / CONTRACT / REVENUE
  private String type;

  private String title;        // 카드 제목
  private String content;      // 한 줄 설명
  private String owner;        // 담당 영업 이름
  private LocalDateTime occurredAt;  // 정렬 기준 시각
}
