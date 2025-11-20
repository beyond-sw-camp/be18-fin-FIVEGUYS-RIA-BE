package com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLogRequestDto {

    private Long actorId;

    private String logName;

    private String resource;

    private String state;
}
