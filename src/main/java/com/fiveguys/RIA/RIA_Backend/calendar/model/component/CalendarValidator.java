package com.fiveguys.RIA.RIA_Backend.calendar.model.component;

import com.fiveguys.RIA.RIA_Backend.calendar.model.dto.request.CalendarRequestDto;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarErrorCode;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarException;
import org.springframework.stereotype.Component;

@Component
public class CalendarValidator {

    /** 메모 생성/수정 시 검증 */
    public void validateMemo(CalendarRequestDto dto) {

        if (dto == null) {
            throw new CalendarException(CalendarErrorCode.INVALID_INPUT_VALUE);
        }

        if (dto.getSummary() != null && dto.getSummary().isBlank()) {
            throw new CalendarException(CalendarErrorCode.INVALID_INPUT_VALUE);
        }
    }

    /** 이메일 형식 검증 */
    public void validateEmail(String email) {

        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new CalendarException(CalendarErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    /** eventId 검증 */
    public void validateEventId(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            throw new CalendarException(CalendarErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
