package com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AdminLogResponseDto {

    private Long logId;

    private Long actorId;
    private String userName;
    private String employeeNo;

    private String logName;
    private String resource;
    private String state;

    private LocalDateTime createdAt;
}
