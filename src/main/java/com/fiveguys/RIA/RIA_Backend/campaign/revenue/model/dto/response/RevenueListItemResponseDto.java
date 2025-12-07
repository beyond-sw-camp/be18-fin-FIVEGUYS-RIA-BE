package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RevenueListItemResponseDto {

    private Long revenueId;
    private Long projectId;
    private Long contractId;
    private Long settlementId;

    private String contractTitle;
    private String clientCompanyName;

    private Integer settlementYear;
    private Integer settlementMonth;

    private BigDecimal finalRevenue;

    private String storeType;

    private Long managerId;
    private String managerName;

    // 추가
    private LocalDate contractStartDay;
    private LocalDate contractEndDay;
}
