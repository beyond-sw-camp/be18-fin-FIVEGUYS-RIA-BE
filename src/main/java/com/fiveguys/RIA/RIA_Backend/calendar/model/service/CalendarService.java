package com.fiveguys.RIA.RIA_Backend.calendar.model.service;

import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response.CalendarResponseDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response.SharedUserResponseDto;

import java.util.List;

public interface CalendarService {

    List<CalendarResponseDto> listEvents();

    CalendarResponseDto createEvent(CalendarRequestDto dto);

    CalendarResponseDto updateEvent(String eventId, CalendarRequestDto dto);

    void deleteEvent(String eventId);

    void addUser(String email, String role);

    void deleteUser(String email);

    List<SharedUserResponseDto> getUsers();
}
