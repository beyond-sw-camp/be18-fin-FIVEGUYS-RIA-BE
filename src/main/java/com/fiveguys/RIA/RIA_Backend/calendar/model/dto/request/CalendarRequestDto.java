package com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarRequestDto {
    private String summary;
    private String description;
    private String location;
    private String startDateTime;
    private String endDateTime;
    private String color;
    private String creatorEmail;
}
