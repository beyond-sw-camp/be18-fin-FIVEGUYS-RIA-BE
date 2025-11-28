package com.fiveguys.RIA.RIA_Backend.notification.model.service;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.NotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.request.NotificationCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.DeleteNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.ReadNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;

import java.util.List;

public interface NotificationService {
    List<BaseNotificationResponseDto> getNotifications(Long receiverId);

    BaseNotificationResponseDto getNotification(Long notificationId, Long receiverId);

    BaseNotificationResponseDto createNotification(Long senderId, Long receiverId, NotificationContext context);

    ReadNotificationResponseDto readNotification(Long notificationId, Long userId);

    List<ReadNotificationResponseDto> readAllNotifications(Long userId);

    DeleteNotificationResponseDto deleteNotification(Long notificationId, Long userId);

    List<DeleteNotificationResponseDto> deleteAllNotification(Long userId);
}
