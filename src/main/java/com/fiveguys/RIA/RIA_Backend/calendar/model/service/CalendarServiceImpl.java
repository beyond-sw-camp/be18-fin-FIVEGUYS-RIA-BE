package com.fiveguys.RIA.RIA_Backend.calendar.model.service;

import com.fiveguys.RIA.RIA_Backend.calendar.model.component.CalendarMapper;
import com.fiveguys.RIA.RIA_Backend.calendar.model.component.CalendarPermissionChecker;
import com.fiveguys.RIA.RIA_Backend.calendar.model.component.GoogleAccessControlClient;
import com.fiveguys.RIA.RIA_Backend.calendar.model.component.GoogleCalendarClient;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response.CalendarResponseDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarErrorCode;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarException;
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
    private final CalendarPermissionChecker permissionChecker;

    /** 📅 모든 메모 조회 */
    @Override
    public List<CalendarResponseDto> listEvents() {
        return calendarClient.listEvents().getItems().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /** 📝 메모 생성 (작성자 이메일 저장) */
    @Override
    public CalendarResponseDto createEvent(CalendarRequestDto dto) {

        if (dto.getSummary() == null || dto.getSummary().isBlank()) {
            throw new CalendarException(CalendarErrorCode.INVALID_INPUT_VALUE);
        }

        // 현재 로그인 사용자 이메일
        String creatorEmail = permissionChecker.getLoginUserEmail();

        Event newEvent = mapper.toGoogleEvent(dto, creatorEmail);
        Event created = calendarClient.createEvent(newEvent);

        return mapper.toResponse(created);
    }

    /** ✏️ 메모 수정 (작성자만 가능) */
    @Override
    public CalendarResponseDto updateEvent(String eventId, CalendarRequestDto dto) {

        Event existing = calendarClient.getEvent(eventId);

        String eventCreatorEmail = null;
        if (existing.getExtendedProperties() != null &&
                existing.getExtendedProperties().getPrivate() != null) {
            eventCreatorEmail = existing.getExtendedProperties().getPrivate().get("creatorEmail");
        }

        // 권한 체크
        permissionChecker.checkOwnerPermission(eventCreatorEmail);

        // DTO 검증
        if (dto.getSummary() != null && dto.getSummary().isBlank()) {
            throw new CalendarException(CalendarErrorCode.INVALID_INPUT_VALUE);
        }

        Event updatedEvent = mapper.applyUpdate(dto, existing);
        Event updated = calendarClient.updateEvent(eventId, updatedEvent);

        return mapper.toResponse(updated);
    }

    /** ❌ 메모 삭제 (작성자만 가능) */
    @Override
    public void deleteEvent(String eventId) {

        Event existing = calendarClient.getEvent(eventId);

        String eventCreatorEmail = null;
        if (existing.getExtendedProperties() != null &&
                existing.getExtendedProperties().getPrivate() != null) {
            eventCreatorEmail = existing.getExtendedProperties().getPrivate().get("creatorEmail");
        }

        // 권한 체크
        permissionChecker.checkOwnerPermission(eventCreatorEmail);

        calendarClient.deleteEvent(eventId);
    }

    /** ➕ 공유 사용자 추가 */
    @Override
    public void addUser(String email, String role) {

        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new CalendarException(CalendarErrorCode.INVALID_EMAIL_FORMAT);
        }

        accessControlClient.addUser(email, role);
    }

    /** ➖ 공유 사용자 삭제 */
    @Override
    public void deleteUser(String email) {

        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new CalendarException(CalendarErrorCode.INVALID_EMAIL_FORMAT);
        }

        accessControlClient.removeUser(email);
    }

    /** 👥 공유 사용자 목록 조회 */
    @Override
    public List<Map<String, String>> getUsers() {
        return accessControlClient.listUsers();
    }
}
