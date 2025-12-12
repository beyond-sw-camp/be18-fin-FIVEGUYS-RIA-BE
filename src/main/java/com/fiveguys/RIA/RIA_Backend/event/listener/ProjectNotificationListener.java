package com.fiveguys.RIA.RIA_Backend.event.listener;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.event.project.ProjectNotificationEvent;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.SalesNotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
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

        Project project = event.getProject();

        // 메시지 생성
        String message = switch (event.getTargetAction()) {
            case CREATED ->
                    event.getSenderName() + " " + event.getSenderRole() + "님이 프로젝트 '" + project.getTitle() + "'을 생성했습니다.";
            case UPDATED ->
                    event.getSenderName() + " " + event.getSenderRole() + "님이 프로젝트 '" + project.getTitle() + "'을 수정했습니다.";
            case DELETED ->
                    event.getSenderName() + " " + event.getSenderRole() + "님이 프로젝트 '" + project.getTitle() + "'을 삭제했습니다.";
            case MANAGER_UPDATED ->
                    event.getSenderName() + " " + event.getSenderRole() + "님이 프로젝트 '" + project.getTitle() + "'의 담당자를 귀하로 변경했습니다.";
            case STAGE_UPDATED ->
                    event.getSenderName() + " " + event.getSenderRole() + "님이 프로젝트 '" + project.getTitle() + "'의 단계를 변경했습니다.";
            default -> "프로젝트 관련 알림이 발생했습니다.";
        };

        SalesNotificationContext context = SalesNotificationContext.builder()
                .targetType(NotificationTargetType.PROJECT)
                .targetAction(event.getTargetAction())
                .targetId(project.getProjectId())
                .message(message)
                .build();

        notificationService.createNotification(
                event.getSenderId(),
                event.getReceiverId(),
                context
        );
    }
}
