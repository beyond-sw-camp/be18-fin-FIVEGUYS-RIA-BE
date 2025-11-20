package com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
