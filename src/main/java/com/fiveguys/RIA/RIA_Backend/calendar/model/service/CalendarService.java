package com.fiveguys.RIA.RIA_Backend.calendar.model.service;


import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response.CalendarResponseDto;

import java.util.List;
import java.util.Map;

public interface CalendarService {

    List<CalendarResponseDto> listEvents() throws Exception;

    CalendarResponseDto createEvent(CalendarRequestDto dto) throws Exception;

    CalendarResponseDto updateEvent(String eventId, CalendarRequestDto dto) throws Exception;

    void deleteEvent(String eventId) throws Exception;

    void addUser(String email, String role) throws Exception;

    void deleteUser(String email) throws Exception;

    List<Map<String, String>> getUsers() throws Exception;
}
