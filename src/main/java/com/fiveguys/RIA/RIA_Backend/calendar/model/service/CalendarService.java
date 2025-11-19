package com.fiveguys.RIA.RIA_Backend.calendar.model.service;

import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;

import java.util.List;
import java.util.Map;

public interface CalendarService {
    List<CalendarRequestDto> listEvents() throws Exception;

    CalendarRequestDto createEvent(CalendarRequestDto dto) throws Exception;

    CalendarRequestDto updateEvent(String eventId, CalendarRequestDto dto) throws Exception;

    void deleteEvent(String eventId) throws Exception;

    void addUser(String email, String role) throws Exception;

    void removeUser(String email) throws Exception;

    List<Map<String, String>> getUsers() throws Exception;

}
