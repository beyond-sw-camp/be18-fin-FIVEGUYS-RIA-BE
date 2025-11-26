package com.fiveguys.RIA.RIA_Backend.notification.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import com.fiveguys.RIA.RIA_Backend.notification.model.repository.NotificationRepository;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSseServiceImpl implements NotificationSseService {

    private final NotificationRepository notificationRepository;

    private static final long DEFAULT_TIMEOUT = 60 * 60 * 1000L; // 1시간

    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    private final Map<Long, LinkedHashMap<String, BaseNotificationResponseDto>> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = userId + "_" + UUID.randomUUID();
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitters.computeIfAbsent(userId, id -> new ConcurrentHashMap<>())
                .put(emitterId, emitter);

        log.info("[SSE SUBSCRIBE] userId={}, emitterId={}, lastEventId={}", userId, emitterId, lastEventId);

        // 1. DB에서 읽지 않은 알림 전송
        sendUnreadNotificationsFromDb(userId, emitter);

        // 2. SSE 캐시에서 누락 이벤트 전송
        if (lastEventId != null && !lastEventId.isEmpty()) {
            resendLostEvents(userId, lastEventId, emitter);
        }

        Runnable cleanup = () -> removeEmitter(userId, emitterId);
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> {
            log.error("[SSE ERROR] userId={}, emitterId={}, message={}", userId, emitterId, e.getMessage());
            cleanup.run();
        });

        // 3. 초기 연결 알림 전송
        sendDummyEvent(emitter, emitterId);

        return emitter;
    }

    private void sendDummyEvent(SseEmitter emitter, String emitterId) {
        try {
            emitter.send(SseEmitter.event()
                    .id("INIT-" + emitterId)
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            log.error("[SSE INIT SEND FAIL] emitterId={}", emitterId);
        }
    }

    private void resendLostEvents(Long userId, String lastEventId, SseEmitter emitter) {
        LinkedHashMap<String, BaseNotificationResponseDto> userEvents = eventCache.get(userId);
        if (userEvents == null) return;

        long lastTs = extractTimestampSafe(lastEventId);

        // lastEventId 이후 누락 이벤트만 전송
        for (Map.Entry<String, BaseNotificationResponseDto> entry : userEvents.entrySet()) {
            if (extractTimestampSafe(entry.getKey()) > lastTs) {
                sendEvent(emitter, "resend", entry.getKey(), entry.getValue());
            }
        }
    }

    private void sendUnreadNotificationsFromDb(Long userId, SseEmitter emitter) {
        List<Notification> unreadNotifications = notificationRepository.findByReceiverIdAndIsReadFalse(userId);

        log.info("[SEND UNREAD NOTIFICATIONS] userId={} - total unread={}", userId, unreadNotifications.size());

        for (Notification notification : unreadNotifications) {
            // mapper
            BaseNotificationResponseDto dto = BaseNotificationResponseDto.builder()
                    .notificationId(notification.getNotificationId())
                    .senderId(notification.getSender().getId())
                    .receiverId(notification.getReceiver().getId())
                    .targetType(notification.getTargetType())
                    .targetAction(notification.getTargetAction())
                    .targetId(notification.getTargetId())
                    .message(notification.getMessage())
                    .isRead(notification.isRead())
                    .createdAt(notification.getCreatedAt())
                    .build();

            String eventId = "DB-" + notification.getNotificationId();

            log.info("[SEND UNREAD NOTIFICATIONS] Sending eventId={} to userId={}, message={}",
                    eventId, userId, notification.getMessage());

            cacheEvent(userId, eventId, dto);

            sendEvent(emitter, "notification", eventId, dto);
        }
    }

    private void sendEvent(SseEmitter emitter, String eventName, String eventId, BaseNotificationResponseDto dto) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name(eventName)
                    .data(dto));
        } catch (IOException e) {
            log.error("[SSE SEND FAIL] eventId={}, message={}", eventId, e.getMessage());
        }
    }

    private long extractTimestampSafe(String eventId) {
        try {
            String[] parts = eventId.split("-");
            for (String part : parts) {
                if (part.matches("\\d{13}")) { // 13자리 밀리초 타임스탬프 찾기
                    return Long.parseLong(part);
                }
            }
        } catch (Exception e) {
            log.warn("[SSE EVENT ID PARSE FAIL] eventId={}", eventId);
        }
        return -1L;
    }

    private void cacheEvent(Long userId, String eventId, BaseNotificationResponseDto notificationDto) {
        eventCache.computeIfAbsent(userId, k -> new LinkedHashMap<String, BaseNotificationResponseDto>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, BaseNotificationResponseDto> eldest) {
                return size() > 100; // 최대 100개까지만 유지
            }
        }).put(eventId, notificationDto);
    }

    @Override
    public void sendNotification(Long receiverId, BaseNotificationResponseDto notificationDto) {
        Map<String, SseEmitter> userEmitters = emitters.get(receiverId);
        String eventId = "ts-" + System.currentTimeMillis() + "-" + UUID.randomUUID();

        cacheEvent(receiverId, eventId, notificationDto);

        if (userEmitters != null) {
            List<String> deadEmitters = new ArrayList<>();
            userEmitters.forEach((emitterId, emitter) -> {
                sendEventWithFailCheck(receiverId, emitterId, emitter, eventId, notificationDto, deadEmitters);
            });

            // 실패한 emitter 제거
            deadEmitters.forEach(userEmitters::remove);
        }
    }

    private void sendEventWithFailCheck(Long userId, String emitterId, SseEmitter emitter, String eventId,
                                        BaseNotificationResponseDto dto, List<String> deadEmitters) {
        try {
            sendEvent(emitter, "notification", eventId, dto);
            log.info("[SSE EVENT SEND] userId={}, emitterId={}, eventId={}", userId, emitterId, eventId);
        } catch (Exception e) {
            log.error("[SSE SEND FAIL] userId={}, emitterId={}, message={}", userId, emitterId, e.getMessage());
            deadEmitters.add(emitterId);
        }
    }

    private void removeEmitter(Long userId, String emitterId) {
        Map<String, SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitterId);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }
}