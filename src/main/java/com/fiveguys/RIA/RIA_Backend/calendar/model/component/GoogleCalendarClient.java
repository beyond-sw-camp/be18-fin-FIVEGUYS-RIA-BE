package com.fiveguys.RIA.RIA_Backend.calendar.model.component;

import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarErrorCode;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleCalendarClient {

    private final GoogleCredentialProvider credentialProvider;

    private static final String CALENDAR_ID =
            "928924a55a86b48bc19f2c175a0642bffe2666393048c3c93ae81b190e1ad39a@group.calendar.google.com";

    private Calendar service() {
        try {
            return credentialProvider.getCalendarService();
        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /** 📅 이벤트 목록 조회 */
    public Events listEvents() {

        try {
            return service().events().list(CALENDAR_ID)
                    .setMaxResults(50)
                    .setSingleEvents(true)
                    .setOrderBy("startTime")
                    .setTimeZone("Asia/Seoul")
                    .execute();

        } catch (GoogleJsonResponseException e) {
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    /** 📌 이벤트 단건 조회 */
    public Event getEvent(String eventId) {

        try {
            return service().events().get(CALENDAR_ID, eventId).execute();

        } catch (GoogleJsonResponseException e) {
            // 404 이벤트 없음
            if (e.getStatusCode() == 404) {
                throw new CalendarException(CalendarErrorCode.MEMO_NOT_FOUND);
            }
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    /** 📝 이벤트 생성 */
    public Event createEvent(Event event) {

        try {
            return service().events().insert(CALENDAR_ID, event).execute();

        } catch (GoogleJsonResponseException e) {
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    /** ✏️ 이벤트 수정 */
    public Event updateEvent(String eventId, Event event) {

        try {
            return service().events().update(CALENDAR_ID, eventId, event).execute();

        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new CalendarException(CalendarErrorCode.MEMO_NOT_FOUND);
            }
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    /** ❌ 이벤트 삭제 */
    public void deleteEvent(String eventId) {

        try {
            service().events().delete(CALENDAR_ID, eventId).execute();

        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new CalendarException(CalendarErrorCode.MEMO_NOT_FOUND);
            }
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    /** 📌 Google API 오류 매핑 함수 (중복 제거) */
    private CalendarException mapGoogleException(GoogleJsonResponseException e) {

        int status = e.getStatusCode();

        return switch (status) {
            case 400 -> new CalendarException(CalendarErrorCode.GOOGLE_API_BAD_REQUEST);
            case 401 -> new CalendarException(CalendarErrorCode.GOOGLE_API_UNAUTHORIZED);
            case 403 -> new CalendarException(CalendarErrorCode.GOOGLE_API_FORBIDDEN);
            case 404 -> new CalendarException(CalendarErrorCode.MEMO_NOT_FOUND);
            case 429 -> new CalendarException(CalendarErrorCode.GOOGLE_API_RATE_LIMIT);
            default -> new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        };
    }
}
