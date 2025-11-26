package com.fiveguys.RIA.RIA_Backend.notification.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class BaseNotificationResponseDto {

    private final Long notificationId;
    private final Long senderId;
    private final Long receiverId;
    private final NotificationTargetType targetType;
    private final NotificationTargetAction targetAction;
    private final Long targetId;
    private final String message;
    private final boolean isRead;
    private final LocalDateTime createdAt;
}
