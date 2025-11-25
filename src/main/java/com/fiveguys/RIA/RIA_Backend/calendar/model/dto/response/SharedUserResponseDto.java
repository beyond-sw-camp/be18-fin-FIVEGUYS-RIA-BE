package com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SharedUserResponseDto {

    private String email;
    private String role;
    private String name;
}
