package com.fiveguys.RIA.RIA_Backend.notification.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationSseService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationSseControllerTest {

    @Mock
    private NotificationSseService sseService;

    @InjectMocks
    private NotificationSseController sseController;

    @Test
    @DisplayName("subscribe: SSE 구독 성공 시 SseEmitter 반환 + 서비스 호출 검증")
    void subscribe_success() {

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 2L;
        given(userDetails.getUserId()).willReturn(userId);

        String lastEventId = "event-10";

        SseEmitter emitter = mock(SseEmitter.class);
        given(sseService.subscribe(userId, lastEventId))
                .willReturn(emitter);

        SseEmitter result = sseController.subscribe(userDetails, lastEventId);

        assertThat(result).isSameAs(emitter);
        verify(sseService).subscribe(userId, lastEventId);
    }
}
