package com.fiveguys.RIA.RIA_Backend.notification.model.service;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationSseService {

    SseEmitter subscribe(Long userId, String lastEventId);

    void sendNotification(Long receiverId, BaseNotificationResponseDto notificationResponseDto);
}
