package com.fiveguys.RIA.RIA_Backend.calendar.model.component;

import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarErrorCode;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.AclRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleAccessControlClient {

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

    /** 📌 공유 사용자 추가 */
    public void addUser(String email, String role) {

        validateEmail(email);

        AclRule rule = new AclRule()
                .setRole(role)
                .setScope(new AclRule.Scope().setType("user").setValue(email));

        try {
            service().acl().insert(CALENDAR_ID, rule).execute();

        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 409) {
                throw new CalendarException(CalendarErrorCode.USER_ALREADY_SHARED);
            }
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }


    /** 📌 공유 사용자 제거 */
    public void removeUser(String email) {

        validateEmail(email);

        try {
            List<AclRule> rules = service().acl().list(CALENDAR_ID).execute().getItems();

            for (AclRule rule : rules) {
                if ("user".equals(rule.getScope().getType()) &&
                        email.equals(rule.getScope().getValue())) {

                    service().acl().delete(CALENDAR_ID, rule.getId()).execute();
                    return;
                }
            }

            // 리스트에 존재하지 않음 → 404로 보내고 싶음
            throw new CalendarException(CalendarErrorCode.USER_NOT_SHARED);

        } catch (CalendarException e) {
            // 👇 이미 우리가 의도한 도메인 예외면 그대로 다시 던짐 (404 유지)
            throw e;

        } catch (GoogleJsonResponseException e) {
            throw mapGoogleException(e);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_API_ERROR);
        }
    }

    /**
     * 📌 공유 사용자 목록 조회
     */
    public List<Map<String, String>> listUsers() {

        try {
            List<AclRule> rules = service().acl().list(CALENDAR_ID).execute().getItems();

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

    /** 📌 이메일 검증 */
    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new CalendarException(CalendarErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    /** 📌 Google API 예외 매핑 공통 처리 */
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
