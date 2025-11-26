package com.fiveguys.RIA.RIA_Backend.notification.model.dto.context;

import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalesNotificationContext implements NotificationContext {
    private final NotificationTargetType notificationTargetType;
    private final NotificationTargetAction notificationTargetAction;
    private final Long targetId;
    private final String message;
}
