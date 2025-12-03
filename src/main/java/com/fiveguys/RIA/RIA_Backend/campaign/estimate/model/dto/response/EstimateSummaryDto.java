package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EstimateSummaryDto {

    private Long estimateId;
    private String title;           // estimateTitle
    private String writerName;      // createdUser.name
    private Long totalAmount;       // 합산 금액
    private LocalDate createdDate;  // createdAt.toLocalDate()
    private String remark;          // 비고
}