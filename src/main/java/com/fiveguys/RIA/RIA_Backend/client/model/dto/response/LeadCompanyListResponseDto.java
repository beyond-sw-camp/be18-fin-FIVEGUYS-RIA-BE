package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LeadCompanyListResponseDto {

    private Long clientCompanyId;     // ID
    private String companyName;       // 회사명
    private String category;          // 업종
    private LocalDateTime createdAt;  // 등록일

    private String mainContactName;   // 담당자명
    private String mainContactPhone;  // 연락처
    private String mainContactEmail;  // 이메일

    private LocalDate latestActivityDate; // 최근 활동일 (프로젝트/제안/견적/계약/담당자등록 중 가장 최근)
}
