package com.fiveguys.RIA.RIA_Backend.notification.model.service;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.NotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;

import java.util.List;

public interface NotificationService {
    List<BaseNotificationResponseDto> getNotifications(Long receiverId);

    BaseNotificationResponseDto getNotification(Long notificationId, Long receiverId);

    BaseNotificationResponseDto createNotification(Long senderId, Long receiverId, NotificationTargetType targetType, NotificationTargetAction targetAction, Long targetId, NotificationContext context);

    BaseNotificationResponseDto readNotification(Long notificationId, Long userId);

    List<BaseNotificationResponseDto> readAllNotifications(Long userId);

    BaseNotificationResponseDto deleteNotification(Long notificationId, Long userId);
}
