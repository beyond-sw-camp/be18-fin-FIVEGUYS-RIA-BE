package com.fiveguys.RIA.RIA_Backend.calendar.model.service;

import com.fiveguys.RIA.RIA_Backend.calendar.model.component.CalendarMapper;
import com.fiveguys.RIA.RIA_Backend.calendar.model.component.GoogleAccessControlClient;
import com.fiveguys.RIA.RIA_Backend.calendar.model.component.GoogleCalendarClient;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response.CalendarResponseDto;
import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final GoogleCalendarClient calendarClient;
    private final GoogleAccessControlClient accessControlClient;
    private final CalendarMapper mapper;

    @Override
    public List<CalendarResponseDto> listEvents() throws Exception {
        return calendarClient.listEvents().getItems().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public CalendarResponseDto createEvent(CalendarRequestDto dto) throws Exception {
        Event newEvent = mapper.toGoogleEvent(dto);
        Event created = calendarClient.createEvent(newEvent);
        return mapper.toResponse(created);
    }

    @Override
    public CalendarResponseDto updateEvent(String eventId, CalendarRequestDto dto) throws Exception {
        Event existing = calendarClient.getEvent(eventId);
        Event updatedEvent = mapper.applyUpdate(dto, existing);
        Event updated = calendarClient.updateEvent(eventId, updatedEvent);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteEvent(String eventId) throws Exception {
        calendarClient.deleteEvent(eventId);
    }

    @Override
    public void addUser(String email, String role) throws Exception {
        accessControlClient.addUser(email, role);
    }

    @Override
    public void deleteUser(String email) throws Exception {
        accessControlClient.removeUser(email);
    }

    @Override
    public List<Map<String, String>> getUsers() throws Exception {
        return accessControlClient.listUsers();
    }
}
