package com.fiveguys.RIA.RIA_Backend.calendar.controller;

import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    /**
     * 📅 모든 이벤트 조회
     */
    @SneakyThrows
    @GetMapping
    public ResponseEntity<List<CalendarRequestDto>> getEvents() {
        return ResponseEntity.ok(calendarService.listEvents());
    }

    /**
     * 📝 새 메모 생성
     */
    @SneakyThrows
    @PostMapping("/memos")
    public ResponseEntity<CalendarRequestDto> createMemo(@RequestBody CalendarRequestDto dto) {
        return ResponseEntity.ok(calendarService.createEvent(dto));
    }

    /**
     * ✏️ 이벤트 수정 (메모 포함)
     */
    @SneakyThrows
    @PutMapping("/memos/{eventId}")
    public ResponseEntity<CalendarRequestDto> updateEvent(
            @PathVariable String eventId,
            @RequestBody CalendarRequestDto dto
    ) {
        return ResponseEntity.ok(calendarService.updateEvent(eventId, dto));
    }

    /**
     * ❌ 이벤트 삭제
     */
    @SneakyThrows
    @DeleteMapping("/memos/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
        calendarService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    /**
     * ➕ 사용자 초대
     */
    @SneakyThrows
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String role = req.getOrDefault("role", "writer");
        calendarService.addUser(email, role);

        return ResponseEntity.ok("사용자 추가 완료: " + email);
    }

    /**
     * ➖ 사용자 제거
     * DELETE Body 금지 → QueryParam 방식으로 변경
     */
    @SneakyThrows
    @DeleteMapping("/users")
    public ResponseEntity<String> removeUser(@RequestParam String email) {
        calendarService.removeUser(email);
        return ResponseEntity.ok("사용자 삭제 완료: " + email);
    }

    /**
     * 👥 공유 사용자 목록 조회
     */
    @SneakyThrows
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, String>>> getUsers() {
        return ResponseEntity.ok(calendarService.getUsers());
    }
}

