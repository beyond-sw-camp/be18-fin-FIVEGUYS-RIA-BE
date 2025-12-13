package com.fiveguys.RIA.RIA_Backend.event.listener;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.event.estimate.EstimateNotificationEvent;
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
public class EstimateNotificationListener {

    private final NotificationService notificationService;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEstimateEvent(EstimateNotificationEvent event) {

        Estimate estimate = event.getEstimate();

        // 메시지 생성
        String message = switch (event.getTargetAction()) {
            case CREATED -> event.getSenderName() + " " + event.getSenderRole() + "님이 견적 '" + estimate.getEstimateTitle() + "'을 생성했습니다.";
            case UPDATED -> event.getSenderName() + " " + event.getSenderRole() + "님이 견적 '" + estimate.getEstimateTitle() + "'을 수정했습니다.";
            case DELETED -> event.getSenderName() + " " + event.getSenderRole() + "님이 견적 '" + estimate.getEstimateTitle() + "'을 삭제했습니다.";
            default -> "견적 관련 알림이 발생했습니다.";
        };

        SalesNotificationContext context = SalesNotificationContext.builder()
                .targetType(NotificationTargetType.ESTIMATE)
                .targetAction(event.getTargetAction())
                .targetId(estimate.getEstimateId())
                .message(message)
                .build();

        notificationService.createNotification(
                event.getSenderId(),
                event.getReceiverId(),
                context
        );
    }

}
