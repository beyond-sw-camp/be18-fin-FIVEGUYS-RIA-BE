package com.fiveguys.RIA.RIA_Backend.notification.model.component.builder;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.NotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.SalesNotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SalesNotificationBuilder implements NotificationBuilder {

    @Override
    public Notification build(User sender, User receiver, NotificationContext notificationContext) {

        SalesNotificationContext context = (SalesNotificationContext) notificationContext;

        NotificationTargetType targetType = context.getNotificationTargetType();
        NotificationTargetAction targetAction = context.getNotificationTargetAction();
        Long targetId = context.getTargetId();
        String message = context.getMessage();

        return Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .targetType(targetType)
                .targetAction(targetAction)
                .targetId(targetId)
                .message(message)
                .build();
    }
}
