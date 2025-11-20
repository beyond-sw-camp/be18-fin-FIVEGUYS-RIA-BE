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

    /**
     * 📌 CreateRequest + creatorEmail → Google Event
     */
    public Event toGoogleEvent(CalendarRequestDto dto, String creatorEmail) {

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

        // 🔥 extendedProperties.private 에 color + creatorEmail 저장
        Map<String, String> props = new HashMap<>();
        if (dto.getColor() != null) {
            props.put("color", dto.getColor());
        }
        if (creatorEmail != null) {
            props.put("creatorEmail", creatorEmail);
        }

        if (!props.isEmpty()) {
            event.setExtendedProperties(
                    new Event.ExtendedProperties().setPrivate(props)
            );
        }

        return event;
    }

    /**
     * 📌 UpdateRequest → 기존 Event 수정 (creatorEmail은 그대로 유지)
     */
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

        // color만 업데이트, creatorEmail은 기존 값 유지
        if (dto.getColor() != null) {
            Map<String, String> props =
                    (base.getExtendedProperties() != null &&
                            base.getExtendedProperties().getPrivate() != null)
                            ? new HashMap<>(base.getExtendedProperties().getPrivate())
                            : new HashMap<>();

            props.put("color", dto.getColor());

            base.setExtendedProperties(
                    new Event.ExtendedProperties().setPrivate(props)
            );
        }

        return base;
    }

    /**
     * 📌 Google Event → Response DTO
     */
    public CalendarResponseDto toResponse(Event event) {

        String start = (event.getStart() != null && event.getStart().getDateTime() != null)
                ? event.getStart().getDateTime().toStringRfc3339()
                : null;

        String end = (event.getEnd() != null && event.getEnd().getDateTime() != null)
                ? event.getEnd().getDateTime().toStringRfc3339()
                : null;

        String color = null;
        String creatorEmail = null;

        if (event.getExtendedProperties() != null &&
                event.getExtendedProperties().getPrivate() != null) {

            Map<String, String> props = event.getExtendedProperties().getPrivate();
            color = props.get("color");
            creatorEmail = props.get("creatorEmail");
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
