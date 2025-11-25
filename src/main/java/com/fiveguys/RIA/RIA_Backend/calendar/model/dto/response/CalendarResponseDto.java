package com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarResponseDto {

    private String id;
    private String summary;
    private String description;
    private String location;
    private String startDateTime;
    private String endDateTime;
    private String color;
    private String creatorEmail;
}
