package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalDetailResponseDto {

  // 상단 기본 정보
  private Long proposalId;
  private String title;                 // 제안명

  // 고객사 & 고객 정보
  private Long clientCompanyId;
  private String clientCompanyName;     // 고객사

  private Long clientId;
  private String clientName;            // 고객

  // 영업 기회(프로젝트)
  private Long projectId;
  private String projectTitle;

  // 담당자
  private Long createdUserId;
  private String createdUserName;

  // 본문 내용
  private String data;                  // 제안 내용

  // 날짜
  private LocalDate requestDate;        // 요청일
  private LocalDate submitDate;         // 제출일

  // 비고
  private String remark;

}
