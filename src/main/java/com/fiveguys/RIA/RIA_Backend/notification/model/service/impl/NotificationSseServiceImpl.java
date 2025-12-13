package com.fiveguys.RIA.RIA_Backend.notification.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.notification.model.component.NotificationMapper;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import com.fiveguys.RIA.RIA_Backend.notification.model.repository.NotificationRepository;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSseServiceImpl implements NotificationSseService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Value("${sse.default-timeout}")
    private long defaultTimeout;

    // userId → emitter (한 유저당 1개)
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // userId → eventCache
    private final Map<Long, LinkedHashMap<String, BaseNotificationResponseDto>> eventCache = new ConcurrentHashMap<>();


    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {

        // 기존 emitter 종료 및 제거
        SseEmitter oldEmitter = emitters.remove(userId);
        if (oldEmitter != null) {
            try {
                oldEmitter.complete();
            } catch (Exception ignored) {}
        }

        String emitterId = userId + "_" + UUID.randomUUID();
        SseEmitter emitter = new SseEmitter(defaultTimeout);
        emitters.put(userId, emitter);

        log.info("[SSE SUBSCRIBE] userId={}, emitterId={}, lastEventId={}", userId, emitterId, lastEventId);

        // DB에 있는 읽지 않은 알림 비동기 전송
        CompletableFuture.runAsync(() -> sendUnreadNotificationsFromDb(userId, emitter));

        // 누락 이벤트 재전송
        if (lastEventId != null && !lastEventId.isEmpty()) {
            CompletableFuture.runAsync(() -> resendLostEvents(userId, lastEventId, emitter));
        }

        Runnable cleanup = () -> removeEmitter(userId);

        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> {
            log.error("[SSE ERROR] userId={}, emitterId={}, message={}", userId, emitterId, e.getMessage());
            cleanup.run();
        });

        // 연결 확인 이벤트
        sendConnectEvent(emitter, emitterId);

        return emitter;
    }

    private void sendConnectEvent(SseEmitter emitter, String emitterId) {
        try {
            emitter.send(SseEmitter.event()
                    .id("INIT-" + emitterId)
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            log.warn("[SSE INIT SEND FAIL] emitterId={}", emitterId);
        }
    }

    @Scheduled(fixedRate = 25_000)
    public void sendGlobalHeartbeat() {
        log.info("[GLOBAL HEARTBEAT] send");
        if (emitters.isEmpty()) {
            log.debug("[SSE HEARTBEAT] no active emitters");
            return;
        }

        log.debug("[SSE HEARTBEAT] activeEmitters={}", emitters.size());

        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("ping")
                        .data("heartbeat"));
            } catch (IOException e) {
                log.warn("[SSE GLOBAL HEARTBEAT FAIL] userId={}, message={}",
                        userId, e.getMessage());
                removeEmitter(userId);
            }
        });
    }

    private void sendUnreadNotificationsFromDb(Long userId, SseEmitter emitter) {
        try {
            List<Notification> unread = notificationRepository
                    .findByReceiverIdAndIsReadFalseAndIsDeletedFalse(userId);

            log.info("[SEND UNREAD] userId={}, count={}", userId, unread.size());

            for (Notification n : unread) {
                BaseNotificationResponseDto dto = notificationMapper.toResponseDto(n);
                String eventId = System.currentTimeMillis() + "-DB-" + n.getNotificationId();
                cacheEvent(userId, eventId, dto);

                // 현재 emitter가 살아있는지 확인
                SseEmitter current = emitters.get(userId);
                if (current == null || current != emitter) break;

                try {
                    emitter.send(SseEmitter.event()
                            .id(eventId)
                            .name("notification")
                            .data(dto));
                } catch (IOException e) {
                    log.error("[SSE SEND FAIL] eventId={}, message={}", eventId, e.getMessage());
                    removeEmitter(userId);
                    break;
                }

                Thread.sleep(5);
            }
        } catch (Exception e) {
            log.error("[SEND UNREAD ASYNC ERROR] userId={}, message={}", userId, e.getMessage());
        }
    }

    private void resendLostEvents(Long userId, String lastEventId, SseEmitter emitter) {
        LinkedHashMap<String, BaseNotificationResponseDto> events = eventCache.get(userId);
        if (events == null) return;

        long lastTs = extractTimestamp(lastEventId);

        for (Map.Entry<String, BaseNotificationResponseDto> entry : events.entrySet()) {
            long currentTs = extractTimestamp(entry.getKey());
            if (currentTs > lastTs) {
                SseEmitter current = emitters.get(userId);
                if (current == null || current != emitter) break;
                sendEvent(emitter, "resend", entry.getKey(), entry.getValue());
            }
        }
    }

    private void sendEvent(SseEmitter emitter, String name, String id, BaseNotificationResponseDto dto) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name(name)
                    .data(dto));
        } catch (IOException e) {
            log.error("[SSE SEND FAIL] eventId={}, message={}", id, e.getMessage());
        }
    }

    private long extractTimestamp(String eventId) {
        try {
            String[] parts = eventId.split("-");
            for (String p : parts) {
                if (p.matches("\\d{13}")) {
                    return Long.parseLong(p);
                }
            }
        } catch (Exception ignored) {}
        return -1;
    }

    private void cacheEvent(Long userId, String eventId, BaseNotificationResponseDto dto) {
        eventCache.computeIfAbsent(userId, k ->
                new LinkedHashMap<String, BaseNotificationResponseDto>() {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<String, BaseNotificationResponseDto> eldest) {
                        return size() > 200;
                    }
                }
        ).put(eventId, dto);
    }

    @Override
    public void sendNotification(Long receiverId, BaseNotificationResponseDto dto) {
        SseEmitter emitter = emitters.get(receiverId);
        if (emitter == null) return;

        String eventId = System.currentTimeMillis() + "-" + UUID.randomUUID();
        cacheEvent(receiverId, eventId, dto);

        // 살아있는 emitter만 사용
        SseEmitter current = emitters.get(receiverId);
        if (current == null || current != emitter) return;

        sendEvent(emitter, "notification", eventId, dto);
    }

    private void removeEmitter(Long userId) {
        SseEmitter emitter = emitters.remove(userId);
        if (emitter != null) {
            try {
                emitter.complete();
            } catch (Exception ignored) {}
            log.info("[SSE EMITTER REMOVED] userId={}", userId);
        }
    }
}
