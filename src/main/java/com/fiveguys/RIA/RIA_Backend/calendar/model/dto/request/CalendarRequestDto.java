package com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarRequestDto {
    private String id;
    private String summary;
    private String description;
    private String location;
    private String startDateTime; // ISO 8601 문자열 e.g. 2025-11-03T10:00:00Z
    private String endDateTime;
    private String color;  // 🔥 추가
    private String creatorEmail;
}
