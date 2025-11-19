package com.fiveguys.RIA.RIA_Backend.calendar.model.component;

import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.response.CalendarResponseDto;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CalendarMapper {

    /* ============================
       CreateRequest → Google Event
       ============================ */
    public Event toGoogleEvent(CalendarRequestDto dto) {
        Event event = new Event();

        if (dto.getSummary() != null) event.setSummary(dto.getSummary());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getLocation() != null) event.setLocation(dto.getLocation());

        if (dto.getStartDateTime() != null) {
            event.setStart(new EventDateTime()
                    .setDateTime(new DateTime(dto.getStartDateTime()))
                    .setTimeZone("Asia/Seoul"));
        }

        if (dto.getEndDateTime() != null) {
            event.setEnd(new EventDateTime()
                    .setDateTime(new DateTime(dto.getEndDateTime()))
                    .setTimeZone("Asia/Seoul"));
        }

        if (dto.getColor() != null) {
            Map<String, String> props = new HashMap<>();
            props.put("color", dto.getColor());
            event.setExtendedProperties(
                    new Event.ExtendedProperties().setPrivate(props)
            );
        }

        return event;
    }

    /* ============================
       UpdateRequest → 기존 Event 업데이트
       ============================ */
    public Event applyUpdate(CalendarRequestDto dto, Event base) {

        if (dto.getSummary() != null) base.setSummary(dto.getSummary());
        if (dto.getDescription() != null) base.setDescription(dto.getDescription());
        if (dto.getLocation() != null) base.setLocation(dto.getLocation());

        if (dto.getStartDateTime() != null) {
            base.setStart(new EventDateTime()
                    .setDateTime(new DateTime(dto.getStartDateTime()))
                    .setTimeZone("Asia/Seoul"));
        }

        if (dto.getEndDateTime() != null) {
            base.setEnd(new EventDateTime()
                    .setDateTime(new DateTime(dto.getEndDateTime()))
                    .setTimeZone("Asia/Seoul"));
        }

        if (dto.getColor() != null) {
            Map<String, String> props = new HashMap<>();
            props.put("color", dto.getColor());
            base.setExtendedProperties(
                    new Event.ExtendedProperties().setPrivate(props)
            );
        }

        return base;
    }

    /* ============================
       Google Event → Response DTO
       ============================ */
    public CalendarResponseDto toResponse(Event event) {

        String start = (event.getStart() != null && event.getStart().getDateTime() != null)
                ? event.getStart().getDateTime().toStringRfc3339()
                : null;

        String end = (event.getEnd() != null && event.getEnd().getDateTime() != null)
                ? event.getEnd().getDateTime().toStringRfc3339()
                : null;

        String color = (event.getExtendedProperties() != null &&
                event.getExtendedProperties().getPrivate() != null)
                ? event.getExtendedProperties().getPrivate().get("color")
                : null;

        String creatorEmail = null;
        if (event.getCreator() != null && event.getCreator().getEmail() != null) {
            String email = event.getCreator().getEmail();
            if (email.contains("@") && !email.contains("gserviceaccount.com")) {
                creatorEmail = email;
            }
        }

        return new CalendarResponseDto(
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
}
