package com.fiveguys.RIA.RIA_Backend.calendar.controller;

import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response.CalendarResponseDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CalendarController {

    private final CalendarService calendarService;

    /** 📅 모든 메모 조회 */
    @GetMapping
    public ResponseEntity<List<CalendarResponseDto>> getEvents() {
        return ResponseEntity.ok(calendarService.listEvents());
    }

    /** 📝 메모 생성 */
    @PostMapping("/memos")
    public ResponseEntity<CalendarResponseDto> createMemo(
            @RequestBody CalendarRequestDto dto) {
        return ResponseEntity.ok(calendarService.createEvent(dto));
    }

    /** ✏️ 메모 수정 */
    @PutMapping("/memos/{eventId}")
    public ResponseEntity<CalendarResponseDto> updateEvent(
            @PathVariable String eventId,
            @RequestBody CalendarRequestDto dto) {
        return ResponseEntity.ok(calendarService.updateEvent(eventId, dto));
    }

    /** ❌ 메모 삭제 */
    @DeleteMapping("/memos/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
        calendarService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    /** ➕ 공유 사용자 추가 */
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String role = req.getOrDefault("role", "writer");

        calendarService.addUser(email, role);
        return ResponseEntity.ok("사용자 추가 완료: " + email);
    }

    /** ➖ 공유 사용자 삭제 */
    @DeleteMapping("/users")
    public ResponseEntity<String> removeUser(@RequestParam String email) {

        calendarService.deleteUser(email);
        return ResponseEntity.ok("사용자 삭제 완료: " + email);
    }

    /** 👥 공유 사용자 목록 조회 */
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, String>>> getUsers() {
        return ResponseEntity.ok(calendarService.getUsers());
    }
}
