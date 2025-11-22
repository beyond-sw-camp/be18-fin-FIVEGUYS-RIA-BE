package com.fiveguys.RIA.RIA_Backend.calendar.model.component;

import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarErrorCode;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.AclRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleAccessControlClient {

    private final GoogleCredentialProvider credentialProvider;

    /** 📌 application.yml → google.calendar.id */
    @Value("${google.calendar.id}")
    private String calendarId;

    private Calendar service() {
        try {
            return credentialProvider.getCalendarService();
        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /** 📌 공유 사용자 추가 */
    public void addUser(String email, String role) {

        validateEmail(email);

        AclRule rule = new AclRule()
                .setRole(role)
                .setScope(new AclRule.Scope().setType("user").setValue(email));

        try {
            service().acl().insert(calendarId, rule).execute();

        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 409) {
                throw new CalendarException(CalendarErrorCode.USER_ALREADY_SHARED);
            }
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    /** 📌 공유 사용자 삭제 */
    public void removeUser(String email) {

        validateEmail(email);

        try {
            List<AclRule> rules = service().acl().list(calendarId).execute().getItems();

            for (AclRule rule : rules) {
                if ("user".equals(rule.getScope().getType()) &&
                        email.equals(rule.getScope().getValue())) {

                    service().acl().delete(calendarId, rule.getId()).execute();
                    return;
                }
            }

            throw new CalendarException(CalendarErrorCode.USER_NOT_SHARED);

        } catch (CalendarException e) {
            throw e;

        } catch (GoogleJsonResponseException e) {
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    /** 📌 공유 사용자 목록 조회 */
    public List<Map<String, String>> listUsers() {

        try {
            List<AclRule> rules = service().acl().list(calendarId).execute().getItems();

            return rules.stream()
                    .filter(rule ->
                            rule.getScope() != null &&
                                    "user".equals(rule.getScope().getType()) &&
                                    rule.getScope().getValue() != null &&
                                    rule.getScope().getValue().contains("@") &&
                                    !rule.getScope().getValue().contains("gserviceaccount.com")
                    )
                    .map(rule -> {
                        String email = rule.getScope().getValue();
                        return Map.of(
                                "email", email,
                                "role", rule.getRole(),
                                "name", email.substring(0, email.indexOf("@"))
                        );
                    })
                    .toList();

        } catch (GoogleJsonResponseException e) {
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new CalendarException(CalendarErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    private CalendarException mapGoogleException(GoogleJsonResponseException e) {

        int code = e.getStatusCode();

        return switch (code) {
            case 400 -> new CalendarException(CalendarErrorCode.GOOGLE_API_BAD_REQUEST);
            case 401 -> new CalendarException(CalendarErrorCode.GOOGLE_API_UNAUTHORIZED);
            case 403 -> new CalendarException(CalendarErrorCode.GOOGLE_API_FORBIDDEN);
            case 429 -> new CalendarException(CalendarErrorCode.GOOGLE_API_RATE_LIMIT);
            default -> new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        };
    }
}
