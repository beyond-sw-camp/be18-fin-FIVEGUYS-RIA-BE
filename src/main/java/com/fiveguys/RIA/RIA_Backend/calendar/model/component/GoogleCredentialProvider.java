package com.fiveguys.RIA.RIA_Backend.calendar.model.component;

import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarErrorCode;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleCredentialProvider {

    @Value("${google.calendar.service-account}")
    private String serviceAccountFile;

    @Value("${google.calendar.application-name}")
    private String applicationName;

    /** 🔥 Singleton Transport (절대 매 요청마다 만들면 안 됨) */
    private final NetHttpTransport httpTransport;

    public GoogleCredentialProvider() {
        try {
            this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_TRANSPORT_ERROR);
        }
    }

    /** 🔥 Credential은 요청마다 생성해도 안전함 */
    public Calendar getCalendarService() {

        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials
                    .fromStream(new FileInputStream(serviceAccountFile))
                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        } catch (IOException e) {
            throw new CalendarException(CalendarErrorCode.SERVICE_ACCOUNT_LOAD_FAILED);
        }

        try {
            return new Calendar.Builder(
                    httpTransport,                       // ✔ 싱글톤 Transport
                    JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            )
                    .setApplicationName(applicationName)
                    .build();

        } catch (CalendarException e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_CREDENTIAL_ERROR);

        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.GOOGLE_TRANSPORT_ERROR);
        }
    }
}
