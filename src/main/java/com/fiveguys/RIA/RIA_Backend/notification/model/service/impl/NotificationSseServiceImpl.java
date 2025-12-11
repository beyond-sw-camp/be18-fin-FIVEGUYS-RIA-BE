package com.fiveguys.RIA.RIA_Backend.notification.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.notification.model.component.NotificationMapper;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import com.fiveguys.RIA.RIA_Backend.notification.model.repository.NotificationRepository;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSseServiceImpl implements NotificationSseService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Value("${sse.default-timeout}")
    private long defaultTimeout;

    // userId → (emitterId → emitter)
    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    // userId → eventCache
    private final Map<Long, LinkedHashMap<String, BaseNotificationResponseDto>> eventCache = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || !userId.equals(((CustomUserDetails) auth.getPrincipal()).getUserId())) {
            log.warn("[SSE SUBSCRIBE DENIED] 인증 실패 userId={}", userId);
            return null;
        }

        String emitterId = userId + "_" + UUID.randomUUID();
        SseEmitter emitter = new SseEmitter(defaultTimeout);

        emitters.computeIfAbsent(userId, id -> new ConcurrentHashMap<>())
                .put(emitterId, emitter);

        log.info("[SSE SUBSCRIBE] userId={}, emitterId={}, lastEventId={}",
                userId, emitterId, lastEventId);

        sendUnreadNotificationsFromDb(userId, emitter);

        if (lastEventId != null && !lastEventId.isEmpty()) {
            resendLostEvents(userId, lastEventId, emitter);
        }

        Runnable cleanup = () -> removeEmitter(userId, emitterId);

        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> {
            log.error("[SSE ERROR] userId={}, emitterId={}, message={}",
                    userId, emitterId, e.getMessage());
            cleanup.run();
        });

        sendConnectEvent(emitter, emitterId);

        startHeartbeat(userId, emitterId, emitter);

        return emitter;
    }


    private void startHeartbeat(Long userId, String emitterId, SseEmitter emitter) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("ping")
                        .data("heartbeat"));
            } catch (IOException e) {
                log.warn("[SSE HEARTBEAT FAIL] userId={}, emitterId={}, message={}",
                        userId, emitterId, e.getMessage());
                removeEmitter(userId, emitterId);
            }
        }, 30, 30, TimeUnit.SECONDS);
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

    private void sendUnreadNotificationsFromDb(Long userId, SseEmitter emitter) {

        List<Notification> unread = notificationRepository
                .findByReceiverIdAndIsReadFalseAndIsDeletedFalse(userId);

        log.info("[SEND UNREAD] userId={}, count={}", userId, unread.size());

        for (Notification n : unread) {
            BaseNotificationResponseDto dto = notificationMapper.toResponseDto(n);

            String eventId = System.currentTimeMillis() + "-DB-" + n.getNotificationId();

            cacheEvent(userId, eventId, dto);
            sendEvent(emitter, "notification", eventId, dto);
        }
    }

    private void resendLostEvents(Long userId, String lastEventId, SseEmitter emitter) {
        LinkedHashMap<String, BaseNotificationResponseDto> events = eventCache.get(userId);
        if (events == null) return;

        long lastTs = extractTimestamp(lastEventId);

        for (Map.Entry<String, BaseNotificationResponseDto> entry : events.entrySet()) {
            long currentTs = extractTimestamp(entry.getKey());
            if (currentTs > lastTs) {
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
                        return size() > 200; // 캐시 크기 증가
                    }
                }
        ).put(eventId, dto);
    }


    @Override
    public void sendNotification(Long receiverId, BaseNotificationResponseDto dto) {

        Map<String, SseEmitter> userEmitters = emitters.get(receiverId);

        String eventId = System.currentTimeMillis() + "-" + UUID.randomUUID();

        cacheEvent(receiverId, eventId, dto);

        if (userEmitters == null) return;

        List<String> deadEmitters = new ArrayList<>();

        userEmitters.forEach((emitterId, emitter) -> {
            try {
                sendEvent(emitter, "notification", eventId, dto);
            } catch (Exception e) {
                deadEmitters.add(emitterId);
                log.error("[SSE SEND FAIL] receiverId={}, emitterId={}, msg={}",
                        receiverId, emitterId, e.getMessage());
            }
        });

        deadEmitters.forEach(userEmitters::remove);
    }

    private void removeEmitter(Long userId, String emitterId) {
        Map<String, SseEmitter> map = emitters.get(userId);
        if (map != null) {
            map.remove(emitterId);
            if (map.isEmpty()) emitters.remove(userId);
        }
        log.info("[SSE EMITTER REMOVED] userId={}, emitterId={}", userId, emitterId);
    }
}
