package com.fiveguys.RIA.RIA_Backend.calendar.controller;


import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response.CalendarResponseDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarErrorCode;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarException;
import com.fiveguys.RIA.RIA_Backend.calendar.model.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping
    public ResponseEntity<List<CalendarResponseDto>> getEvents() throws Exception {
        return ResponseEntity.ok(calendarService.listEvents());
    }

    @PostMapping("/memos")
    public ResponseEntity<CalendarResponseDto> createMemo(
            @RequestBody CalendarRequestDto dto) throws Exception {
        return ResponseEntity.ok(calendarService.createEvent(dto));
    }

    @PutMapping("/memos/{eventId}")
    public ResponseEntity<CalendarResponseDto> updateEvent(
            @PathVariable String eventId,
            @RequestBody CalendarRequestDto dto) throws Exception {
        return ResponseEntity.ok(calendarService.updateEvent(eventId, dto));
    }

    @DeleteMapping("/memos/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) throws Exception {
        calendarService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody Map<String, String> req) throws Exception {
        String email = req.get("email");
        String role = req.getOrDefault("role", "writer");
        calendarService.addUser(email, role);
        return ResponseEntity.ok("사용자 추가 완료: " + email);
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> removeUser(@RequestParam(required = false) String email) throws Exception {

        if (email == null || email.isBlank()) {
            throw new CalendarException(CalendarErrorCode.INVALID_EMAIL_FORMAT);
        }

        calendarService.deleteUser(email);
        return ResponseEntity.ok("사용자 삭제 완료: " + email);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, String>>> getUsers() throws Exception {
        return ResponseEntity.ok(calendarService.getUsers());
    }
}
