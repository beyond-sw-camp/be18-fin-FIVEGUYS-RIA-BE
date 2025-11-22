package com.fiveguys.RIA.RIA_Backend.calendar.model.service;

import com.fiveguys.RIA.RIA_Backend.calendar.model.component.CalendarMapper;
import com.fiveguys.RIA.RIA_Backend.calendar.model.component.CalendarPermissionChecker;
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
    private final CalendarPermissionChecker permissionChecker;

    /** 📅 모든 메모 조회 */
    @Override
    public List<CalendarResponseDto> listEvents() {
        return calendarClient.listEvents()
                .getItems()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /** 📝 메모 생성 (로그인한 사용자 이메일 저장) */
    @Override
    public CalendarResponseDto createEvent(CalendarRequestDto dto) {

        // 1) 로그인 사용자 이메일
        String creatorEmail = permissionChecker.getLoginUserEmail();

        // 2) Google Event 생성
        Event newEvent = mapper.toGoogleEvent(dto, creatorEmail);

        // 3) Google Calendar에 저장
        Event created = calendarClient.createEvent(newEvent);

        // 4) 응답 DTO 변환
        return mapper.toResponse(created);
    }

    /** ✏️ 메모 수정 (작성자만 허용) */
    @Override
    public CalendarResponseDto updateEvent(String eventId, CalendarRequestDto dto) {

        // 1) 기존 메모 가져오기
        Event existing = calendarClient.getEvent(eventId);

        // 2) 기존 메모 작성자 email 추출
        String eventCreatorEmail = null;
        if (existing.getExtendedProperties() != null &&
                existing.getExtendedProperties().getPrivate() != null) {
            eventCreatorEmail = existing.getExtendedProperties().getPrivate().get("creatorEmail");
        }

        // 3) 권한 체크
        permissionChecker.checkOwnerPermission(eventCreatorEmail);

        // 4) 메모 수정
        Event updatedEvent = mapper.applyUpdate(dto, existing);
        Event updated = calendarClient.updateEvent(eventId, updatedEvent);

        return mapper.toResponse(updated);
    }

    /** ❌ 메모 삭제 (작성자만 가능) */
    @Override
    public void deleteEvent(String eventId) {

        // 1) 기존 event 조회
        Event existing = calendarClient.getEvent(eventId);

        // 2) 작성자 email 추출
        String eventCreatorEmail = null;
        if (existing.getExtendedProperties() != null &&
                existing.getExtendedProperties().getPrivate() != null) {
            eventCreatorEmail = existing.getExtendedProperties().getPrivate().get("creatorEmail");
        }

        // 3) 권한 체크
        permissionChecker.checkOwnerPermission(eventCreatorEmail);

        // 4) 삭제 실행
        calendarClient.deleteEvent(eventId);
    }

    /** ➕ 공유 사용자 추가 */
    @Override
    public void addUser(String email, String role) {
        accessControlClient.addUser(email, role);
    }

    /** ➖ 공유 사용자 삭제 */
    @Override
    public void deleteUser(String email) {
        accessControlClient.removeUser(email);
    }

    /** 👥 공유 사용자 목록 조회 */
    @Override
    public List<Map<String, String>> getUsers() {
        return accessControlClient.listUsers();
    }
}
