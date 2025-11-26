package com.fiveguys.RIA.RIA_Backend.event.listener;

import com.fiveguys.RIA.RIA_Backend.event.project.ProjectNotificationEvent;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.SalesNotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProjectNotificationListener {

    private final NotificationService notificationService;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleProjectEvent(ProjectNotificationEvent event) {

        // 메시지 자동 생성
        String message = switch (event.getAction()) {
            case CREATED -> "새 프로젝트가 생성되었습니다: " + event.getTitle();
            case UPDATED -> "프로젝트가 수정되었습니다: " + event.getTitle();
            case DELETED -> "프로젝트가 삭제되었습니다: " + event.getTitle();
            default -> "프로젝트 알림: " + event.getTitle();
        };

        // SalesNotificationContext 생성 (type + action 구조)
        SalesNotificationContext context = SalesNotificationContext.builder()
                .notificationTargetType(NotificationTargetType.PROJECT)
                .notificationTargetAction(event.getAction())   // UPDATED도 가능!
                .targetId(event.getProjectId())
                .message(message)
                .build();

        // 알림 생성
        notificationService.createNotification(
                event.getSenderId(),
                event.getReceiverId(),
                NotificationTargetType.PROJECT,
                event.getAction(),            // CREATED or UPDATED
                event.getProjectId(),
                context
        );
    }
}
