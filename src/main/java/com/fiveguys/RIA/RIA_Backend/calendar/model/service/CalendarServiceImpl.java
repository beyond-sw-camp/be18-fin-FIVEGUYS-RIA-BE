package com.fiveguys.RIA.RIA_Backend.calendar.model.service;

import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CalendarServiceImpl implements CalendarService {
    @Value("${google.calendar.service-account}")
    private String serviceAccountFile;

    @Value("${google.calendar.application-name}")
    private String applicationName;

    private final String CALENDAR_ID =
            "928924a55a86b48bc19f2c175a0642bffe2666393048c3c93ae81b190e1ad39a@group.calendar.google.com";

    private Calendar calendarService;

    @PostConstruct
    public void init() throws Exception {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountFile))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        calendarService = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(applicationName)
                .build();
    }

    // 📌 1. 일정 목록 조회
    @Override
    public List<CalendarRequestDto> listEvents() throws Exception {
        Events events = calendarService.events().list(CALENDAR_ID)
                .setMaxResults(50)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .setTimeZone("Asia/Seoul")
                .execute();

        List<CalendarRequestDto> dtoList = new ArrayList<>();

        if (events.getItems() != null) {
            for (Event e : events.getItems()) {
                dtoList.add(convertToDTO(e));
            }
        }
        return dtoList;
    }

    // 📌 2. 일정 생성
    @Override
    public CalendarRequestDto createEvent(CalendarRequestDto dto) throws Exception {

        if (dto.getSummary() == null || dto.getSummary().isBlank()) {
            dto.setSummary("메모");
        }

        Event event = new Event()
                .setSummary(dto.getSummary())
                .setDescription(dto.getDescription())
                .setLocation(dto.getLocation())
                .setStart(
                        new EventDateTime()
                                .setDateTime(new com.google.api.client.util.DateTime(dto.getStartDateTime()))
                                .setTimeZone("Asia/Seoul")
                )
                .setEnd(
                        new EventDateTime()
                                .setDateTime(new com.google.api.client.util.DateTime(dto.getEndDateTime()))
                                .setTimeZone("Asia/Seoul")
                );

        if (dto.getColor() != null) {
            Map<String, String> props = new HashMap<>();
            props.put("color", dto.getColor());
            event.setExtendedProperties(new Event.ExtendedProperties().setPrivate(props));
        }

        Event created = calendarService.events().insert(CALENDAR_ID, event).execute();
        return convertToDTO(created);
    }

    // 📌 3. 일정 수정
    @Override
    public CalendarRequestDto updateEvent(String eventId, CalendarRequestDto dto) throws Exception {

        Event existingEvent = calendarService.events().get(CALENDAR_ID, eventId).execute();

        if (dto.getSummary() != null) existingEvent.setSummary(dto.getSummary());
        if (dto.getDescription() != null) existingEvent.setDescription(dto.getDescription());
        if (dto.getLocation() != null) existingEvent.setLocation(dto.getLocation());

        if (dto.getStartDateTime() != null) {
            existingEvent.setStart(
                    new EventDateTime()
                            .setDateTime(new com.google.api.client.util.DateTime(dto.getStartDateTime()))
                            .setTimeZone("Asia/Seoul")
            );
        }

        if (dto.getEndDateTime() != null) {
            existingEvent.setEnd(
                    new EventDateTime()
                            .setDateTime(new com.google.api.client.util.DateTime(dto.getEndDateTime()))
                            .setTimeZone("Asia/Seoul")
            );
        }

        if (dto.getColor() != null) {
            Map<String, String> props = new HashMap<>();
            props.put("color", dto.getColor());
            existingEvent.setExtendedProperties(new Event.ExtendedProperties().setPrivate(props));
        }

        Event updated = calendarService.events().update(CALENDAR_ID, eventId, existingEvent).execute();
        return convertToDTO(updated);
    }

    // 📌 4. 일정 삭제
    @Override
    public void deleteEvent(String eventId) throws Exception {
        calendarService.events().delete(CALENDAR_ID, eventId).execute();
    }

    // 📌 5. DTO 변환
    private CalendarRequestDto convertToDTO(Event event) {
        String start = null;
        if (event.getStart() != null && event.getStart().getDateTime() != null)
            start = event.getStart().getDateTime().toStringRfc3339();

        String end = null;
        if (event.getEnd() != null && event.getEnd().getDateTime() != null)
            end = event.getEnd().getDateTime().toStringRfc3339();

        String color = null;
        if (event.getExtendedProperties() != null &&
                event.getExtendedProperties().getPrivate() != null) {
            color = event.getExtendedProperties().getPrivate().get("color");
        }

        String creatorEmail = null;

        if (event.getCreator() != null) {
            String email = event.getCreator().getEmail();

            if (email != null) {
                boolean hasAt = email.contains("@");
                boolean isServiceAccount = email.contains("gserviceaccount.com");
                boolean isHexId = email.matches("^[a-fA-F0-9]{24,}$");

                if (hasAt && !isServiceAccount && !isHexId) {
                    creatorEmail = email;
                }
            }
        }

        return new CalendarRequestDto(
                event.getId(),
                event.getSummary(),
                event.getDescription(),
                event.getLocation(),
                start,
                end,
                color,
                creatorEmail
        );
    }

    // 📌 6. 공유 사용자 추가
    @Override
    public void addUser(String userEmail, String role) throws Exception {
        AclRule rule = new AclRule();
        AclRule.Scope scope = new AclRule.Scope();
        scope.setType("user");
        scope.setValue(userEmail);
        rule.setScope(scope);
        rule.setRole(role);

        calendarService.acl().insert(CALENDAR_ID, rule).execute();
    }

    // 📌 7. 공유 사용자 제거
    @Override
    public void removeUser(String userEmail) throws Exception {

        List<AclRule> rules = calendarService.acl().list(CALENDAR_ID).execute().getItems();

        for (AclRule rule : rules) {
            if (rule.getScope() != null &&
                    "user".equals(rule.getScope().getType()) &&
                    userEmail.equals(rule.getScope().getValue())) {

                calendarService.acl().delete(CALENDAR_ID, rule.getId()).execute();
                return;
            }
        }
    }

    // 📌 8. 공유 사용자 목록 조회
    @Override
    public List<Map<String, String>> getUsers() throws Exception {
        List<Map<String, String>> result = new ArrayList<>();

        List<AclRule> rules = calendarService.acl().list(CALENDAR_ID).execute().getItems();

        for (AclRule rule : rules) {
            if (rule.getScope() == null) continue;
            if (!"user".equals(rule.getScope().getType())) continue;

            String email = rule.getScope().getValue();
            String role = rule.getRole();

            if (email == null || !email.contains("@")) continue;
            if (email.contains("gserviceaccount.com")) continue;
            if (email.matches("^[a-fA-F0-9]{24,}$")) continue;

            Map<String, String> user = new HashMap<>();
            user.put("email", email);
            user.put("role", role);
            user.put("name", email.substring(0, email.indexOf("@")));

            result.add(user);
        }

        return result;
    }
}
